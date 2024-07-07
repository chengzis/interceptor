package com.chengzis.interceptor.view

import android.content.Context
import android.util.Size
import android.view.ViewGroup

interface BlueprintView {

    val context: Context

}





class BlueprintView2(
    override val context: Context,
) : ViewGroup(context), BlueprintView {

    private var mMeasuredSize = Size(0, 0)

    private val mChildren = mutableListOf<BlueprintChild>()

    override fun addBlueprint(blueprint: Blueprint, width: BlueprintSize, height: BlueprintSize) {
        addView(blueprint.view)
        mChildren.add(BlueprintChild(blueprint, width, height))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val availableWidth = MeasureSpec.getSize(widthMeasureSpec)
        val availableHeight = MeasureSpec.getSize(heightMeasureSpec)
        mChildren.forEach {
            it.blueprint.measure(
                widthMeasureSpec = it.width.measureSpec(availableWidth),
                heightMeasureSpec = it.height.measureSpec(availableHeight)
            )
        }
        val size = Size(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )

        mMeasuredSize = blueprint.measure(size)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        blueprint.layout(
            left = l,
            top = t,
            right = l + mMeasuredSize.width,
            bottom = t + mMeasuredSize.height
        )
    }
}
