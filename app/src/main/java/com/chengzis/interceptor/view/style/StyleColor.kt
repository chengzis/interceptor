package com.chengzis.interceptor.view.style

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

sealed interface StyleColor : Style<Int> {


    data class Rgb(@androidx.annotation.ColorInt val color: Int) : StyleColor {
        override fun create(context: StyleContext): Int {
            return color
        }
    }


    data class Res(@ColorRes val resId: Int) : StyleColor {
        override fun create(context: StyleContext): Int {
            return ContextCompat.getColor(context.context, resId)
        }
    }


}