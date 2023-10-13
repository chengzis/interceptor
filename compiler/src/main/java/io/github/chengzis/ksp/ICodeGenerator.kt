package io.github.chengzis.ksp

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

/**
 * 代码生成
 */
interface ICodeGenerator<T> {

    /**
     * 生成代码
     *
     * @param environment   ksp环境
     */
    fun generate(environment: SymbolProcessorEnvironment) : T

}