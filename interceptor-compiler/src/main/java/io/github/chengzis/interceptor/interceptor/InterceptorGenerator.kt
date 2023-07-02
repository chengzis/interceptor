package io.github.chengzis.interceptor.interceptor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import io.github.chengzis.interceptor.ICodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.writeTo
import io.github.chengzis.interceptor.AddInterceptor
import com.neusoft.android.hilt.extension.compiler.interceptor.InterceptorMetadata

/**
 * 生成拦截器
 *
 * 示例代码：
 * ```
 * public interface IUserRepositoryInterceptor<T : IUserRepositoryArgs, R> {
 *
 *     public fun invoke(chain: Chain<T, R>, args: T): R
 *
 *     public class Chain<T : IUserRepositoryArgs, R>(
 *         private val interceptors: List<IUserRepositoryInterceptor<T, R>>
 *     ) {
 *         public fun invoke(args: T): R {
 *             if (interceptors.isEmpty()) {
 *                 throw NullPointerException("最后一个必须返回结果")
 *             }
 *             val interceptor = interceptors.first()
 *             val chain = Chain<T, R>(interceptors.subList(1, interceptors.size))
 *             return interceptor.invoke(chain, args)
 *         }
 *     }
 * }
 * ```
 * @see AddInterceptor
 */
internal class InterceptorGenerator(private val metadata: InterceptorMetadata) :
    ICodeGenerator<Unit> {

    override fun generate(environment: SymbolProcessorEnvironment) {

        val spec = TypeSpec
            .interfaceBuilder(metadata.baseInterceptorTypeName)
            .addTypeVariable(TypeVariableName("T", metadata.baseArgsTypeName))
            .addTypeVariable(TypeVariableName("R"))

        spec.addFunction(
            FunSpec.builder("invoke")
                .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
                .addParameter(
                    "chain",
                    metadata.baseInterceptorChainTypeName.parameterizedBy(
                        TypeVariableName("T", metadata.baseArgsTypeName),
                        TypeVariableName("R")
                    )
                )
                .addParameter("args", TypeVariableName("T"))
                .returns(TypeVariableName("R"))
                .build()
        )

        spec.addType(ChainGenerator(metadata).generate(environment))

        FileSpec.get(metadata.clazz.packageName, spec.build())
            .writeTo(environment.codeGenerator, false)
    }

    private class ChainGenerator(
        private val metadata: InterceptorMetadata
    ) : ICodeGenerator<TypeSpec> {
        override fun generate(environment: SymbolProcessorEnvironment): TypeSpec {
            return TypeSpec
                .classBuilder("Chain")
                .addTypeVariable(TypeVariableName("T", metadata.baseArgsTypeName))
                .addTypeVariable(TypeVariableName("R"))
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(
                            "interceptors", List::class.asClassName()
                                .parameterizedBy(
                                    metadata.baseInterceptorTypeName.parameterizedBy(
                                        TypeVariableName("T"),
                                        TypeVariableName("R")
                                    )
                                )
                        )
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(
                        "interceptors", List::class.asClassName()
                            .parameterizedBy(
                                metadata.baseInterceptorTypeName.parameterizedBy(
                                    TypeVariableName("T"),
                                    TypeVariableName("R")
                                )
                            )
                    ).addModifiers(KModifier.PRIVATE)
                        .initializer("interceptors")
                        .build()
                )

                .addFunction(
                    FunSpec.builder("invoke")
                        .addModifiers(KModifier.PUBLIC)
                        .addParameter("args", TypeVariableName("T", metadata.baseArgsTypeName))
                        .returns(TypeVariableName("R"))
                        .beginControlFlow("if (interceptors.isEmpty())")
                        .addStatement(
                            "throw %T(\"最后一个必须返回结果\")",
                            NullPointerException::class
                        )
                        .endControlFlow()
                        .addStatement("val interceptor = interceptors.first()")
                        .addStatement("val chain = Chain<T, R>(interceptors.subList(1, interceptors.size))")
                        .addStatement("return interceptor.invoke(chain, args)")
                        .build()
                )
                .build()
        }
    }
}