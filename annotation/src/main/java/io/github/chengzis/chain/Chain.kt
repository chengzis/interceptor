package io.github.chengzis.chain

/**
 * 责任链
 * @param interceptors 拦截器列表
 * @param T 方法参数
 * @param R 方法返回值
 */
class Chain<T, R>(private val interceptors: List<Interceptor<T, R>>) {

    fun process(args: T): R {
        if (interceptors.isEmpty()) {
            throw RuntimeException("最后一个拦截器必须返回值，而不是调用chain.process(args)")
        }
        val chain = Chain(interceptors.subList(1, interceptors.size))
        return interceptors.first().process(chain, args)
    }
}