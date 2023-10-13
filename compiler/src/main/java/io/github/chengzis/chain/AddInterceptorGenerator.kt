package io.github.chengzis.chain

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import firstUppercase
import io.github.chengzis.ksp.ICodeGenerator
import io.github.chengzis.medatata.FuncMetadata

/**
 * 添加拦截器
 */
class AddInterceptorGenerator(private val metadata: ChainMetadata) : ICodeGenerator<List<FunSpec>> {

    override fun generate(environment: SymbolProcessorEnvironment): List<FunSpec> {
        val result = mutableListOf<FunSpec>()
        metadata.functions.filterNot { it.ignore }.forEach {
            result.add(generateFunc(it))
            result.add(generateExt(it))
        }
        return result
    }

    private fun generateFunc(func: FuncMetadata): FunSpec {
        val builder = FunSpec.builder("add${func.simpleName.firstUppercase()}Interceptor")
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)

        builder.addKdoc("添加[%L]拦截器\n", func.simpleName)
        builder.addKdoc("@param priority 优先级 数字越大，优先级越高\n")
        builder.addKdoc("@param interceptor 拦截器\n")
        builder.addKdoc("@see %L", func.simpleName)

        builder.addParameter(ParameterSpec.builder("priority", Int::class).build())

        val argsClassName = metadata.argsClassName.nestedClass(func.simpleName.firstUppercase())
        val interceptorClassName = Interceptor::class.asClassName()
            .parameterizedBy(argsClassName, func.returnType)
        builder.addParameter(ParameterSpec.builder("interceptor", interceptorClassName).build())

        builder.addStatement("return ${metadata.interceptorsPropertyName}.${func.simpleName}.add(priority, interceptor)")

        return builder.build()
    }
    private fun generateExt(func: FuncMetadata) : FunSpec {
        val builder = FunSpec.builder("add${func.simpleName.firstUppercase()}Interceptor")
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)

        builder.addKdoc("添加[%L]拦截器\n", func.simpleName)
        builder.addKdoc("@param priority 优先级 数字越大，优先级越高\n")
        builder.addKdoc("@param interceptor 拦截器\n")
        builder.addKdoc("@see %L", func.simpleName)

        builder.addParameter(ParameterSpec.builder("priority", Int::class).build())

        val argsClassName = metadata.argsClassName.nestedClass(func.simpleName.firstUppercase())
        val chainClass = Chain::class.asClassName()
            .parameterizedBy(argsClassName, func.returnType)

        val blockTypeName = LambdaTypeName.get(parameters = arrayListOf(ParameterSpec.unnamed(chainClass), ParameterSpec.unnamed(argsClassName)), returnType = func.returnType)
        builder.addParameter(ParameterSpec("block", blockTypeName))

        builder.addStatement("return ${metadata.interceptorsPropertyName}.${func.simpleName}.add(priority, block)")

        return builder.build()


    }

}