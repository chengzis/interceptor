package io.github.chengzis.chain

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import firstUppercase
import io.github.chengzis.ksp.ICodeGenerator
import io.github.chengzis.medatata.FuncMetadata

class ArgsGenerator(private val metadata: ChainMetadata) : ICodeGenerator<TypeSpec> {

    override fun generate(environment: SymbolProcessorEnvironment): TypeSpec {
        val builder = TypeSpec.interfaceBuilder(metadata.argsClassName)
            .addModifiers(KModifier.PUBLIC, KModifier.SEALED)
            .addKdoc("参数")

        builder.addProperty(
            PropertySpec.builder("obj", metadata.className)
                .build()
        )

        for (function in metadata.functions.filterNot { it.ignore }) {
            builder.addType(generateSubArgs(function))
        }

        return builder.build()
    }

    private fun generateSubArgs(func: FuncMetadata): TypeSpec {
        val builder = TypeSpec.classBuilder(metadata.argsClassName.nestedClass(func.simpleName.firstUppercase()))
            .addSuperinterface(metadata.argsClassName)
            .addModifiers(KModifier.PUBLIC, KModifier.DATA)
            .addProperty(
                PropertySpec.builder("obj", metadata.className)
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer("obj")
                    .build()
            )
        func.doc?.let {
            builder.addKdoc(it)
        }
        builder.addKdoc("@see %T.${func.simpleName}", metadata.className)

        val primaryConstructorBuilder = FunSpec.constructorBuilder()
            .addParameter(ParameterSpec("obj", metadata.className))

        func.parameters.forEach {
            builder.addProperty(
                PropertySpec.builder(it.name!!, it.type).initializer(it.name!!).build()
            )
            primaryConstructorBuilder.addParameter(
                ParameterSpec(it.name!!, it.type)
            )
        }

        builder.primaryConstructor(primaryConstructor = primaryConstructorBuilder.build())

        return builder.build()
    }

}