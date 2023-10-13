package io.github.chengzis.chain

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import firstUppercase
import io.github.chengzis.ksp.ICodeGenerator
import io.github.chengzis.medatata.FuncMetadata

class ExFuncGenerator(private val metadata: ChainMetadata) : ICodeGenerator<List<FunSpec>> {

    override fun generate(environment: SymbolProcessorEnvironment): List<FunSpec> {
        return metadata.functions.filterNot { it.ignore }.map { generateFunc(it)}.toList()
    }

    private fun generateFunc(func: FuncMetadata): FunSpec {
        val builder = FunSpec.builder("on${func.simpleName.firstUppercase()}")
            .addModifiers(KModifier.ABSTRACT, KModifier.PROTECTED)
            .returns(func.returnType)
            .addParameters(func.parameters.map { ParameterSpec(it.name!!, it.type) })

        func.doc?.let {
            builder.addKdoc(it)
            builder.addKdoc("\n")
        }
        builder.addKdoc("@see ${func.simpleName}")
        return builder.build()
    }

}