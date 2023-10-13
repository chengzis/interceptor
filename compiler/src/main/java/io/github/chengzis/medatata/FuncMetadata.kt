package io.github.chengzis.medatata

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * 方法元数据
 */
class FuncMetadata(
    override val element: KSFunctionDeclaration,
) : Metadata<KSFunctionDeclaration>() {

    val simpleName get() = element.simpleName.asString()

    val returnType by lazy { element.returnType?.toTypeName() ?: UNIT }

    val parameters by lazy {
        element.parameters.map { ParameterMetadata(it) }
    }

    val doc get() = element.docString?.trimIndent()
}