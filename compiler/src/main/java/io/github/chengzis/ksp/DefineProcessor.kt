package io.github.chengzis.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class DefineProcessor : IAnnotationProcessor {

    override fun process(environment: SymbolProcessorEnvironment, resolver: Resolver) : List<KSAnnotated> {
        val logger = Logger(environment.logger, DefineProcessor::class, "process")
        val elements =
            resolver.getSymbolsWithAnnotation("io.github.chengzis.ksp.Define")

        for (symbol in elements) {
            if (symbol !is KSClassDeclaration) {
                logger.error("被Define修饰的类必须是Class", symbol)
                break
            }
            if (!symbol.simpleName.asString().startsWith("Define")) {
                logger.error("被Define修饰的类的类名必须以Define开始", symbol)
                break
            }
        }

        return emptyList()
    }
}