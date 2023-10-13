package io.github.chengzis.chain

/**
 * 拦截器
 */
interface Interceptor<T, R> {

    fun process(chain: Chain<T, R>, args: T): R

}