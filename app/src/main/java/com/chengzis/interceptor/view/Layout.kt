package com.chengzis.interceptor.view

import android.content.res.Configuration
import android.graphics.Color
import android.view.View
import com.chengzis.interceptor.view.style.StyleColor
import com.chengzis.interceptor.view.style.StyleContext
import com.chengzis.interceptor.view.style.StyleDrawable



interface BlueprintStyle {

    fun setBackground(background: StyleDrawable)


}

open class ViewStyle(private val view: View) : BlueprintStyle {

    private var mBackground:StyleDrawable = StyleDrawable.Color(StyleColor.Rgb(Color.TRANSPARENT))

    override fun setBackground(background: StyleDrawable) {
        mBackground = background
        view.background = mBackground.create(view.context)
    }

    fun onConfigurationChanged(configuration: Configuration) {
        rebind()
    }

    protected open fun rebind() {
        view.background = mBackground.create(view.context)
    }
}

class ViewBlueprintScope(private val view: View) : BlueprintStyle {

    override fun setBackground(background: StyleDrawable) {
        view.background = background.create(view.context)
    }




}






