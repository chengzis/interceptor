package io.github.chengzis.medatata

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ksp.toTypeName

class PropertyMetadata(
    override val element: KSPropertyDeclaration,
) : Metadata<KSPropertyDeclaration>() {

    val simpleName get() = element.simpleName.asString()

    val type by lazy { element.type.toTypeName() }

    val doc get() = element.docString
}