package io.github.chengzis.interceptor.interceptor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import io.github.chengzis.interceptor.AddInterceptor
import com.neusoft.android.hilt.extension.compiler.interceptor.InterceptorMetadata
import io.github.chengzis.interceptor.ICodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.writeTo
import firstUppercase

/**
 * 生成 base类
 * ```
 * public abstract class HasInterceptorIUserRepository : IUserRepository {
 *     internal val findUserByIdInterceptors: ArrayList<IUserRepositoryInterceptor<IUserRepositoryArgs.FindUserById, User>> = ArrayList()
 *
 *     final override fun findUserById(id: String): User {
 *         val interceptors = ArrayList(findUserByIdInterceptors)
 *         interceptors.add(object :
 *             IUserRepositoryInterceptor<IUserRepositoryArgs.FindUserById, User> {
 *             override fun invoke(
 *                 chain: IUserRepositoryInterceptor.Chain<IUserRepositoryArgs.FindUserById, User>, args: IUserRepositoryArgs.FindUserById
 *             ): User {
 *                 return onFindUserById(args.id)
 *             }
 *         })
 *         val args = IUserRepositoryArgs.FindUserById(this, id)
 *         return IUserRepositoryInterceptor.Chain(interceptors).invoke(args)
 *     }
 *
 *     protected abstract fun onFindUserById(id: String): User
 * }
 * ```
 * @see AddInterceptor
 */
internal class HasInterceptorClassGenerator(private val metadata: InterceptorMetadata) :
    ICodeGenerator<Unit> {

    override fun generate(environment: SymbolProcessorEnvironment) {

        val spec = TypeSpec
            .classBuilder(metadata.baseHasInterceptorTypeName)
            .addSuperinterface(metadata.clazz)
            .addModifiers(KModifier.ABSTRACT)
            .addKdoc("@see %T", metadata.clazz)

        if (metadata.baseInterface != null && metadata.baseInterface != UNIT) {
            val clazz = metadata.baseInterface
            spec.superclass(clazz.peerClass("HasInterceptor${clazz.simpleName}"))
        }

        metadata.funcList.forEach { func ->
            spec.addProperty(
                PropertySpec
                    .builder(func.interceptorsFieldName, ArrayList::class.asClassName().parameterizedBy(
                        metadata.baseInterceptorTypeName.parameterizedBy(
                            metadata.baseArgsTypeName.nestedClass(func.name.firstUppercase()),
                            func.returnType
                        )
                    ))
                    .mutable(false)
                    .addModifiers(KModifier.INTERNAL)
                    .initializer("ArrayList()")
                    .build()
            )

            spec.addFunctions(
                OverrideFuncGenerator(metadata, func).generate(environment)
            )
        }

       FileSpec.get(metadata.clazz.packageName, spec.build())
            .writeTo(environment.codeGenerator, false)
    }

    private class OverrideFuncGenerator(
        private val metadata: InterceptorMetadata,
        private val func: InterceptorMetadata.Func,
    ) : ICodeGenerator<List<FunSpec>> {
        val argsType = metadata.baseArgsTypeName.nestedClass(func.firstUppercaseName)

        val overrideFunc = FunSpec.builder(func.name)
            .addModifiers(KModifier.OVERRIDE, KModifier.FINAL)
            .addParameters(func.parameters.map { it.spec })
            .returns(func.returnType)
            .addStatement("val interceptors = %T(%L)", ArrayList::class, func.interceptorsFieldName)
            .addStatement("""
                interceptors.add(object : %T<%T, %T> {
                    override fun invoke(chain : %T, args: %T): %T {
                        return on%L(%L)
                    }
                })
            """.trimIndent(),
                metadata.baseInterceptorTypeName,
                metadata.baseArgsTypeName.nestedClass(func.firstUppercaseName),
                func.returnType,
                metadata.baseInterceptorChainTypeName.parameterizedBy(argsType, func.returnType),
                metadata.baseArgsTypeName.nestedClass(func.firstUppercaseName),
                func.returnType,
                func.firstUppercaseName,
                func.parameters.joinToString(", ") {
                    "args.${it.name}"
                }
            )
            .addStatement("val args = %T(this, %L)",
                metadata.baseArgsTypeName.nestedClass(func.firstUppercaseName),
                func.parameters.toList().joinToString(", "){ it.name })
            .addStatement("return %T(interceptors).invoke(args)", metadata.baseInterceptorChainTypeName)
            .build()

        val onFunc = FunSpec.builder("on${func.firstUppercaseName}")
            .addModifiers(KModifier.PROTECTED, KModifier.ABSTRACT)
            .addParameters(func.parameters.map { it.spec })
            .returns(func.returnType)
            .addKdoc("@see ${func.name}")
            .build()

        override fun generate(environment: SymbolProcessorEnvironment): List<FunSpec> {
           return listOf(overrideFunc, onFunc)
        }
    }
}