package io.github.chengzis.chain

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.writeTo
import io.github.chengzis.chain.ksp.Chains
import io.github.chengzis.ksp.IAnnotationProcessor
import io.github.chengzis.ksp.Logger
import io.github.chengzis.medatata.ClassMetadata

/**
 * [Chains]处理器
 */
class ChainProcessor : IAnnotationProcessor {

    override fun process(
        environment: SymbolProcessorEnvironment,
        resolver: Resolver
    ): List<KSAnnotated> {
        val logger = Logger(environment.logger, ChainProcessor::class, "process")

        val elements =
            resolver.getSymbolsWithAnnotation(Chains::class.qualifiedName!!)
        for (symbol in elements) {
            if (symbol !is KSClassDeclaration || symbol.classKind != ClassKind.INTERFACE) {
                logger.error("被Chains修饰的类必须是接口", symbol)
                break
            }
        }
        for (element in elements) {
            element as KSClassDeclaration

            val metadata = ChainMetadata(ClassMetadata(element))
            ChainGenerator(metadata).generate(environment)
                .writeTo(environment.codeGenerator, false)

        }

        return emptyList()
    }

}