package com.chengzis.interceptor.view.style

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

sealed interface StyleDrawable : Style<Drawable> {

    data class Color(val color: StyleColor) : StyleDrawable {
        override fun create(context: StyleContext): Drawable {
            return ColorDrawable(color.create(context))
        }
    }


    data class Res(@DrawableRes val resId: Int) : StyleDrawable {
        override fun create(context: StyleContext): Drawable {
            return requireNotNull(ContextCompat.getDrawable(context.context, resId))
        }
    }


}