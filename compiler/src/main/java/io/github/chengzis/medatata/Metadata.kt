package io.github.chengzis.medatata

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.chengzis.ksp.Ignore
import kotlin.reflect.KClass

sealed class Metadata<T : KSAnnotated> {

    abstract val element: T

    @OptIn(KspExperimental::class)
    fun <T : Annotation> findAnnotations(type: KClass<T>) = element.getAnnotationsByType(type)

    @OptIn(KspExperimental::class)
    val ignore by lazy { element.isAnnotationPresent(Ignore::class) }



}


