package io.github.chengzis.medatata

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * 方法参数
 */
class ParameterMetadata(override val element: KSValueParameter) : Metadata<KSValueParameter>() {

    val name get() = element.name?.asString()

    val type by lazy { element.type.toTypeName() }

}