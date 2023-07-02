package io.github.chengzis.interceptor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.chengzis.interceptor.interceptor.InterceptorProcessor

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

        InterceptorProcessor().process(environment, resolver)

        return emptyList()
    }
}