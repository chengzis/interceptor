package io.github.chengzis.interceptor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode
import kotlin.reflect.KClass

/**
 * 日志打印
 */
class Logger(private val logger: KSPLogger, clazz: KClass<*>, private val method: String) {

    private val clazzName = clazz.simpleName!!

    fun info(msg: String) {
        logger.warn("$clazzName >> $method >> $msg")
    }

    fun error(msg: String, node: KSNode?) {
        logger.error("$clazzName >> $method >> $msg", node)
    }
}