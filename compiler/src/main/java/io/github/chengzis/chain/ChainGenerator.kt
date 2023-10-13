package io.github.chengzis.chain

import com.google.devtools.ksp.KSTypeNotPresentException
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import io.github.chengzis.ksp.ICodeGenerator

class ChainGenerator(private val metadata: ChainMetadata) : ICodeGenerator<FileSpec> {

    @OptIn(KspExperimental::class)
    override fun generate(environment: SymbolProcessorEnvironment): FileSpec {
        val builder = TypeSpec.classBuilder(metadata.className)
            .addModifiers(KModifier.ABSTRACT, KModifier.PUBLIC)

        try {
            val extend = metadata.chain.extend.asClassName()
            if (extend != UNIT) {
                builder.superclass(extend.peerClass(extend.simpleName.replace("Define", "")))
            }
        } catch (e: KSTypeNotPresentException) {
            val extend = e.ksType.toClassName()
            builder.superclass(extend.peerClass(extend.simpleName.replace("Define", "")))
        }

        metadata.doc?.let {
            builder.addKdoc(it)
            builder.addKdoc("\n")
        }
        builder.addKdoc("@see %T", metadata.defineClassName)

        builder.addProperty(
            PropertySpec.builder(metadata.interceptorsPropertyName, metadata.interceptorsClassName)
                .addModifiers(KModifier.PRIVATE)
                .addKdoc("拦截器")
                .initializer("${metadata.interceptorsClassName.simpleName}()")
                .build()
        )

        builder.addType(ArgsGenerator(metadata).generate(environment))
        builder.addType(InterceptorsGenerator(metadata).generate(environment))
        builder.addFunctions(ExFuncGenerator(metadata).generate(environment))
        builder.addFunctions(OverrideFuncGenerator(metadata).generate(environment))
        builder.addFunctions(AddInterceptorGenerator(metadata).generate(environment))




        return FileSpec.get(metadata.packageName, builder.build())
    }

}