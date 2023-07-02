package com.neusoft.android.hilt.extension.compiler.interceptor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import firstUppercase

data class InterceptorMetadata(
    val clazz: ClassName,
    val baseInterface: ClassName?,
    val docString: String?,
    val funcList: Sequence<Func>,
) {
    /**
     * base
     */
    val baseHasInterceptorTypeName = clazz.peerClass("HasInterceptor${clazz.simpleName}")

    /**
     * base Args
     */
    val baseArgsTypeName = ClassName.bestGuess("${clazz.canonicalName}Args")

    /**
     * base Interceptor
     */
    val baseInterceptorTypeName = ClassName.bestGuess("${clazz.canonicalName}Interceptor")

    /**
     * base Interceptor chain
     */
    val baseInterceptorChainTypeName = baseInterceptorTypeName.nestedClass("Chain")

    data class Func(
        val name: String,
        val docString: String?,
        val parameters: List<Parameter>,
        val returnType: TypeName,
    ) {

        val interceptorsFieldName = name + "Interceptors"

        val firstUppercaseName = name.firstUppercase()
    }

    data class Parameter(
        val name: String,
        val type: TypeName,
    ) {

        val spec = ParameterSpec.builder(name, type).build()

    }
}