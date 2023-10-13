package io.github.chengzis.chain

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asClassName
import firstUppercase
import io.github.chengzis.ksp.ICodeGenerator
import io.github.chengzis.medatata.FuncMetadata

/**
 * Interceptors字段
 */
class InterceptorsGenerator(private val metadata: ChainMetadata) : ICodeGenerator<TypeSpec> {

    override fun generate(environment: SymbolProcessorEnvironment): TypeSpec {
        val builder = TypeSpec.classBuilder(metadata.interceptorsClassName)
            .addModifiers(KModifier.PRIVATE)
            .addKdoc("拦截器")

        for (function in metadata.functions.filterNot { it.ignore }) {
            builder.addProperty(generateProperty(function))
        }

        return builder.build()
    }

    private fun generateProperty(func: FuncMetadata): PropertySpec {
        val args = metadata.argsClassName.nestedClass(func.simpleName.firstUppercase())
        val returnType = func.returnType
        val type = InterceptorList::class.asClassName()
            .parameterizedBy(args, returnType)

        return PropertySpec.builder(func.simpleName, type)
            .initializer("InterceptorList()")
            .addKdoc("[%L]拦截器列表\n", func.simpleName)
            .addKdoc("@see %L\n", func.simpleName)
            .addKdoc("@see %L", "add${func.simpleName.firstUppercase()}Interceptor")
            .build()
    }

}