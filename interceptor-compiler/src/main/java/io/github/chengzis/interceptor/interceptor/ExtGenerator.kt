package io.github.chengzis.interceptor.interceptor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import io.github.chengzis.interceptor.AddInterceptor
import com.neusoft.android.hilt.extension.compiler.interceptor.InterceptorMetadata
import io.github.chengzis.interceptor.ICodeGenerator
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo
import firstUppercase

/**
 * 生成扩展方法
 * ```
 * public fun IUserRepository.addFindUserByIdInterceptor(interceptor: IUserRepositoryInterceptor<IUserRepositoryArgs.FindUserById, User>) {
 *     if (this is HasInterceptorIUserRepository) {
 *         findUserByIdInterceptors.add(interceptor)
 *     }
 * }
 * ```
 * @see AddInterceptor
 */
internal class ExtGenerator(private val metadata: InterceptorMetadata) : ICodeGenerator<Unit> {

    override fun generate(environment: SymbolProcessorEnvironment) {
        val file = FileSpec.builder(metadata.clazz.packageName, "${metadata.clazz.simpleName}Ext")
            .addAnnotation(
                AnnotationSpec.builder(JvmName::class)
                    .addMember("%S", "${metadata.clazz.simpleName}Ext")
                    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                    .build()
            )


        metadata.funcList.forEach { func ->
            file.addFunction(ExtFuncGenerator(metadata, func).generate(environment))
        }

        file.build().writeTo(environment.codeGenerator, false)
    }

    private class ExtFuncGenerator(
        private val metadata: InterceptorMetadata,
        private val func: InterceptorMetadata.Func,
    ) : ICodeGenerator<FunSpec> {
        override fun generate(environment: SymbolProcessorEnvironment): FunSpec {
            return  FunSpec.builder("add${func.name.firstUppercase()}Interceptor")
                .receiver(metadata.clazz)
                .addParameter(
                    "interceptor",
                    metadata.baseInterceptorTypeName
                        .parameterizedBy(
                            metadata.baseArgsTypeName.nestedClass(func.name.firstUppercase()),
                            func.returnType
                        )
                )
                .beginControlFlow("if (this is ${metadata.baseHasInterceptorTypeName.simpleName})")
                .addStatement("${func.name}Interceptors.add(interceptor)")
                .endControlFlow()
                .build()
        }
    }
}