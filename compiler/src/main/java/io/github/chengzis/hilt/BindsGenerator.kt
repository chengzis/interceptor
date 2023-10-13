package io.github.chengzis.hilt

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ANNOTATION
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.ksp.writeTo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import firstUppercase
import io.github.chengzis.ksp.ICodeGenerator

/**
 * Binds组件生成
 */
class BindsGenerator(private val metadata: BindsMetadata) : ICodeGenerator<Unit> {

    override fun generate(environment: SymbolProcessorEnvironment): Unit {
        val packageName = environment.options["MODULE_PACKAGE"]?.trim()
        if (packageName.isNullOrBlank()) {
            environment.logger.error("请设置一个非空的MODULE_PACKAGE")
            return
        }

        val moduleName = environment.options["MODULE_NAME"]?.trim()
        if (moduleName.isNullOrBlank()) {
            environment.logger.error("请设置一个非空的MODULE_NAME")
            return
        }
        val classSimpleName = moduleName.firstUppercase() + metadata.installIn.simpleName
        val builder = TypeSpec.classBuilder(classSimpleName)
            .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
            .addAnnotation(Module::class)
            .addAnnotation(
                AnnotationSpec.builder(InstallIn::class)
                    .addMember("%T::class", metadata.installIn)
                    .build()
            )

        metadata.binds.forEach {
            builder.addFunctions(generateFunc(it))
        }

        if (builder.funSpecs.isEmpty()) {
            return
        }

        FileSpec.builder(packageName, classSimpleName)
            .addType(builder.build())
            .build().writeTo(environment.codeGenerator, false)
    }

    private fun generateFunc(binds: AutoBindsMetadata): List<FunSpec> {
        return binds.targets.map {
            val builder = FunSpec.builder(binds.bindFuncName + it.simpleName)
                .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)
                .addAnnotation(Binds::class)
                .returns(it)
                .addParameter(ParameterSpec("obj", binds.implClassName))

            if (binds.qualifier != ANNOTATION) {
                builder.addAnnotation(binds.qualifier)
            }

            builder.build()
        }
    }

}