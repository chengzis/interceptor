package io.github.chengzis.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * 注解处理器
 */
interface IAnnotationProcessor {

    /**
     * 处理
     *
     * @param environment   ksp环境
     * @param resolver      编译器访问工具
     */
    fun process(environment: SymbolProcessorEnvironment, resolver: Resolver) : List<KSAnnotated>

}