package io.github.chengzis.chain

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asClassName
import firstUppercase
import io.github.chengzis.ksp.ICodeGenerator
import io.github.chengzis.medatata.FuncMetadata

class OverrideFuncGenerator(private val metadata: ChainMetadata) : ICodeGenerator<List<FunSpec>> {

    override fun generate(environment: SymbolProcessorEnvironment): List<FunSpec> {
        return metadata.functions.map {
            if (it.ignore) {
                generateOverrideFunc(it)
            } else {
                generateFunc(it)
            }

        }.toList()
    }

    private fun generateOverrideFunc(func: FuncMetadata): FunSpec  {
        return FunSpec.builder(func.simpleName)
            .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
            .returns(func.returnType)
            .addParameters(func.parameters.map { ParameterSpec(it.name!!, it.type) })
            .apply {
                func.doc?.let {
                    addKdoc(it)
                    addKdoc("\n")
                }
                addKdoc("@see %T.%L", metadata.defineClassName, func.simpleName)
            }
            .build()
    }


    private fun generateFunc(func: FuncMetadata): FunSpec {
        val builder = FunSpec.builder(func.simpleName)
            .addModifiers(KModifier.FINAL, KModifier.PUBLIC)
            .returns(func.returnType)
            .addParameters(func.parameters.map { ParameterSpec(it.name!!, it.type) })

        func.doc?.let {
            builder.addKdoc(it)
            builder.addKdoc("\n")
        }
        builder.addKdoc("@see %T.%L", metadata.defineClassName, func.simpleName)

        val argsClassName = metadata.argsClassName.nestedClass(func.simpleName.firstUppercase())

        builder.addStatement(
            "val args = %T(this, %L)",
            argsClassName,
            func.parameters.map { it.name }.joinToString(", ")
        )
        builder.addStatement(
            "val interceptors = %T(${metadata.interceptorsPropertyName}.${func.simpleName})",
            ArrayList::class
        )

        val interceptorClassName = Interceptor::class.asClassName()
            .parameterizedBy(argsClassName, func.returnType)
        val chainClassName = Chain::class.asClassName()
            .parameterizedBy(argsClassName, func.returnType)


        val defaultBlock = TypeSpec.anonymousClassBuilder()
            .addSuperinterface(interceptorClassName)
            .addFunction(
                FunSpec.builder("process")
                    .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)
                    .returns(func.returnType)
                    .addParameter(ParameterSpec("chain", chainClassName))
                    .addParameter(ParameterSpec("args", argsClassName))
                    .apply {
                        val call = "on${func.simpleName.firstUppercase()}(%L)"
                        val args = func.parameters.map { it.name }.joinToString(", ")
                        addStatement("return $call", args)
                    }
                    .build()
            ).build()
        builder.addStatement("interceptors.add(%L)", defaultBlock)
            .addStatement("val chain = Chain(interceptors)")
            .addStatement("return chain.process(args)")
        return builder.build()
    }

}