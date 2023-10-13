package io.github.chengzis.chain

/**
 * 拦截器集合
 */
class InterceptorList<T, R> : List<Interceptor<T, R>> {

    private val nodes = mutableListOf<Node<T, R>>()

    /**
     * 拦截器的节点
     */
    private inner class Node<T, R>(val priority: Int, val interceptor: Interceptor<T, R>) :
        Comparable<Node<T, R>> {
        override fun compareTo(other: Node<T, R>): Int {
            return priority.compareTo(other.priority)
        }
    }

    fun add(priority: Int, interceptor: Interceptor<T, R>) {
        nodes.add(Node(priority, interceptor))
        nodes.sortDescending()
    }

    fun add(priority: Int, block: (Chain<T, R>, T) -> R) = add(priority, LambdaInterceptor(block))

    override val size: Int
        get() = nodes.size

    override fun contains(element: Interceptor<T, R>): Boolean {
        return nodes.any { it.interceptor == element }
    }

    override fun containsAll(elements: Collection<Interceptor<T, R>>): Boolean {
        return nodes.map { it.interceptor }.containsAll(elements)
    }

    override fun get(index: Int): Interceptor<T, R> {
        return nodes[index].interceptor
    }

    override fun indexOf(element: Interceptor<T, R>): Int {
        return nodes.indexOfFirst { it.interceptor == element }
    }

    override fun isEmpty(): Boolean {
        return nodes.isEmpty()
    }

    override fun iterator(): Iterator<Interceptor<T, R>> {
        return nodes.map { it.interceptor }.iterator()
    }

    override fun lastIndexOf(element: Interceptor<T, R>): Int {
        return nodes.indexOfLast { it.interceptor == element }
    }

    override fun listIterator(): ListIterator<Interceptor<T, R>> {
        return nodes.map { it.interceptor }.listIterator()
    }

    override fun listIterator(index: Int): ListIterator<Interceptor<T, R>> {
        return nodes.map { it.interceptor }.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Interceptor<T, R>> {
        return nodes.subList(fromIndex, toIndex).map { it.interceptor }
    }

    private class LambdaInterceptor<T, R>(private val block: (Chain<T, R>, T) -> R) :
        Interceptor<T, R> {
        override fun process(chain: Chain<T, R>, args: T): R {
            return block.invoke(chain, args)
        }
    }
}