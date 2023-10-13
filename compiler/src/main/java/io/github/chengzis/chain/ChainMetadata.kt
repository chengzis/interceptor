package io.github.chengzis.chain

import io.github.chengzis.chain.ksp.Chains
import io.github.chengzis.medatata.ClassMetadata

class ChainMetadata(element: ClassMetadata) {

    val chain = checkNotNull(element.findAnnotations(Chains::class)).first()

    val defineClassName = element.className

    /**
     * 包名
     */
    val packageName = element.packageName

    /**
     * 生成的目标类的简称
     */
    val simpleName = element.className.simpleName.replace("Define", "")

    /**
     * 生成的目标类的类名
     */
    val className = element.className.peerClass(simpleName)

    /**
     * 生成的目标参数类的类名
     */
    val argsClassName = className.nestedClass("Args")

    /**
     * 生成的目标拦截器类的类名
     */
    val interceptorsClassName = className.nestedClass("Interceptors")

    val functions = element.functions

    val doc = element.doc?.trimIndent()

    /**
     * 拦截器字段名称
     */
    val interceptorsPropertyName = "interceptors"

    override fun toString(): String {
        return "ChainMetadata(simpleName='$simpleName', className=$className, argsClassName=$argsClassName)"
    }


}