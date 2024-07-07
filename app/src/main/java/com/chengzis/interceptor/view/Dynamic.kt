package com.chengzis.interceptor.view

import android.view.View
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

class Dynamic<T>(initialValue: T) {

    private var mField by object : ObservableProperty<T>(initialValue) {

        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean {
            return oldValue != newValue
        }

        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            onChanged.forEach {
                it.invoke(oldValue, newValue)
            }
        }

    }

    var value: T
        get() = mField
        set(value) {
            mField = value
        }

    private val onChanged = mutableListOf<(oldValue: T, newValue: T) -> Unit>()

    fun bindView(view: View, block: (oldValue: T, newValue: T) -> Unit) {
        view.doOnAttach {
            onChanged.add(block)
        }
        view.doOnDetach {
            onChanged.remove(block)
        }
    }

}