package com.chengzis.interceptor.view

import android.content.Context
import android.content.res.Configuration
import android.util.Size
import androidx.appcompat.widget.AppCompatTextView

class Text(
    context: Context,
    private val width: BlueprintSize,
    private val height: BlueprintSize,
    text: String,
    color: Int,
    private val horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    private val verticalAlignment: Alignment.Vertical = Alignment.Top,
) : AppCompatTextView(context),
    Blueprint {

    private val mStyle = ViewStyle(this)

    init {
        setBackgroundColor(color)
        this.text = text
    }

    override fun addToBlueprintView(blueprintView: BlueprintView) {
        blueprintView.addToViewGroup(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (width is BlueprintSize.DynamicSize) {
            width.data.bindView(this) { oldValue, newValue ->
                requestLayout()
            }
        }
        if (height is BlueprintSize.DynamicSize) {
            height.data.bindView(this) { oldValue, newValue ->
                requestLayout()
            }
        }
    }

    override fun measure(available: Size): Size {
        measure(width.measureSpec(available.width), height.measureSpec(available.height))

        return Size(measuredWidth, measuredHeight)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

    }
}


interface TextScope : BlueprintStyle {

}

fun BlueprintView.Text() : Text {
    val view = Text(context)
    addToViewGroup(view)



}


