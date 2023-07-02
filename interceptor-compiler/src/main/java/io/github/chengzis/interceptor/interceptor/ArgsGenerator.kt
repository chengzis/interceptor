package io.github.chengzis.interceptor.interceptor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.neusoft.android.hilt.extension.compiler.interceptor.InterceptorMetadata
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import firstLowercase
import firstUppercase
import io.github.chengzis.interceptor.AddInterceptor
import io.github.chengzis.interceptor.ICodeGenerator

/**
 * 生成方法参数类
 * ```
 * public sealed class IUserRepositoryArgs {
 *
 *     public abstract val iUserRepository: IUserRepository
 *
 *     public data class FindUserById(
 *         override val iUserRepository: IUserRepository,
 *         public val id: String,
 *     ) : IUserRepositoryArgs()
 * }
 * ```
 * @see AddInterceptor
 */
internal class ArgsGenerator(private val metadata: InterceptorMetadata) : ICodeGenerator<Unit> {

    override fun generate(environment: SymbolProcessorEnvironment): Unit {

        val baseType = TypeSpec
            .classBuilder(metadata.baseArgsTypeName)
            .addModifiers(KModifier.SEALED)
            .addProperty(
                PropertySpec
                    .builder(metadata.clazz.simpleName.firstLowercase(), metadata.clazz)
                    .addModifiers(KModifier.ABSTRACT)
                    .addKdoc(metadata.docString.orEmpty())
                    .addKdoc("@see ${metadata.clazz.simpleName}")
                    .build()
            )

        metadata.funcList.forEach { func ->
            baseType.addType(SubArgsGenerator(metadata, func).generate(environment))
        }

        FileSpec.get(metadata.clazz.packageName, baseType.build())
            .writeTo(environment.codeGenerator, false)
    }

    private class SubArgsGenerator(
        private val metadata: InterceptorMetadata,
        private val func: InterceptorMetadata.Func,
    ) : ICodeGenerator<TypeSpec> {
        override fun generate(environment: SymbolProcessorEnvironment): TypeSpec {
            return TypeSpec
                .classBuilder(func.name.firstUppercase())
                .addModifiers(KModifier.DATA)
                .superclass(metadata.baseArgsTypeName)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(
                            metadata.clazz.simpleName.firstLowercase(),
                            metadata.clazz
                        )
                        .addParameters(
                            func.parameters.map { p -> ParameterSpec(p.name, p.type) }
                        )
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(
                        metadata.clazz.simpleName.firstLowercase(),
                        metadata.clazz
                    )
                        .initializer(metadata.clazz.simpleName.firstLowercase())
                        .addModifiers(KModifier.OVERRIDE)
                        .build()
                )
                .addProperties(
                    func.parameters.map { p ->
                        PropertySpec.builder(p.name, p.type).initializer(p.name).build()
                    }
                )
                .build()
        }
    }
}