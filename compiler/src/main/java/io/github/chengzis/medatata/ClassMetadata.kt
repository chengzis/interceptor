package io.github.chengzis.medatata

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName
import io.github.chengzis.ksp.Ignore

/**
 * class元数据
 */
class ClassMetadata(
    override val element: KSClassDeclaration,
) : Metadata<KSClassDeclaration>() {

    /**
     * 包名
     */
    val packageName = element.packageName.asString()

    val simpleName get() = element.simpleName.asString()

    /**
     * 类名
     */
    val className = element.toClassName()

    val doc get() = element.docString

    /**
     * 方法列表
     */
    @OptIn(KspExperimental::class)
    val functions: Sequence<FuncMetadata> by lazy {
        element.getDeclaredFunctions().map { FuncMetadata(it) }
    }

    /**
     * 属性列表
     */
    @OptIn(KspExperimental::class)
    val properties: Sequence<PropertyMetadata> by lazy {
        element.getDeclaredProperties().map { PropertyMetadata(it) }
    }
}