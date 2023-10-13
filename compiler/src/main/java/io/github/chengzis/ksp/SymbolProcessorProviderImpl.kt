package io.github.chengzis.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.chengzis.chain.ChainProcessor
import io.github.chengzis.hilt.AutoBindsProcessor

/**
 * ksp代码处理器提供者
 */
class SymbolProcessorProviderImpl : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SymbolProcessorImpl(environment)
    }
}

/**
 * ksp代码处理器
 */
private class SymbolProcessorImpl(private val environment: SymbolProcessorEnvironment) :
    SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val result = mutableListOf<KSAnnotated>()

        result.addAll(DefineProcessor().process(environment, resolver))
        result.addAll(ChainProcessor().process(environment, resolver))
        result.addAll(AutoBindsProcessor().process(environment, resolver))

        return result
    }
}