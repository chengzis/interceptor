package io.github.chengzis.hilt

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.chengzis.hilt.ksp.AutoBinds
import io.github.chengzis.ksp.IAnnotationProcessor
import io.github.chengzis.ksp.Logger
import io.github.chengzis.medatata.ClassMetadata

/**
 * [AutoBinds]处理器
 */
class AutoBindsProcessor : IAnnotationProcessor {

    override fun process(environment: SymbolProcessorEnvironment, resolver: Resolver) : List<KSAnnotated> {
        val logger = Logger(environment.logger, AutoBindsProcessor::class, "process")

        val elements =
            resolver.getSymbolsWithAnnotation(AutoBinds::class.qualifiedName!!)
        for (symbol in elements) {
            if (symbol !is KSClassDeclaration) {
                logger.error("被AutoBinds修饰的类必须是Class", symbol)
                break
            }
        }

        val metadataList =
            elements.map { AutoBindsMetadata(ClassMetadata(it as KSClassDeclaration)) }

        metadataList.groupBy {
            it.installIn
        }.forEach {
            val metadata = BindsMetadata(it.key, it.value)
            BindsGenerator(metadata).generate(environment)
        }

        return emptyList()
    }
}