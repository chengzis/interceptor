package io.github.chengzis.hilt

import com.google.devtools.ksp.KSTypeNotPresentException
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ANNOTATION
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import io.github.chengzis.hilt.ksp.AutoBinds
import io.github.chengzis.medatata.ClassMetadata


class AutoBindsMetadata(val metadata: ClassMetadata) {

    private val args =
        metadata.element.annotations.first { it.shortName.asString() == AutoBinds::class.simpleName }

    val installIn by lazy {
        val value = args.arguments.first { it.name?.asString() == AutoBinds::installIn.name }.value as KSType
        value.toClassName()
    }

    @OptIn(KspExperimental::class)
    val targets: List<ClassName> by lazy {
        val value = args.arguments.first { it.name?.asString() == AutoBinds::targets.name }.value as ArrayList<*>
        value.map {
            try {
                (it as KSType).toClassName()
            } catch (e: KSTypeNotPresentException) {
                e.ksType.toClassName()
            }
        }
    }

    @OptIn(KspExperimental::class)
    val qualifier by lazy {
        try {
            val value = args.arguments.first { it.name?.asString() == AutoBinds::qualifier.name }.value
            (value as KSType).toClassName()
        } catch (e: KSTypeNotPresentException) {
            e.ksType.toClassName()
        }
    }

    val bindFuncName by lazy {

        val name = "bind"
        if (qualifier == ANNOTATION) {
            name
        } else {
            "${name}With${qualifier.simpleName}"
        }
    }


    val implClassName = metadata.className


//    val qualifier = binds.qualifier


}