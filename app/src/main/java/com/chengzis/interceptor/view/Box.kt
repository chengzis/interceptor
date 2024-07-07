package com.chengzis.interceptor.view

import android.content.Context
import android.util.Size

class Box(
    context: Context,
) : BlueprintViewGroup<BoxLayoutConstraint>(context) {

    private var mMeasuredSize = listOf<Size>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val available = getAvailableSize(widthMeasureSpec, heightMeasureSpec)
        mMeasuredSize = children.map {
            it.measure(available, it.constraint.width, it.constraint.height)
        }






    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {






    }
}


class BoxLayoutConstraint(
    width: BlueprintSize,
    height: BlueprintSize,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) : LayoutConstraint(width, height)