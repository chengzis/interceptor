package com.chengzis.interceptor.view

import android.content.Context
import android.util.Size
import android.view.ViewGroup

abstract class BlueprintViewGroup<T : LayoutConstraint>(
    context: Context,
) : ViewGroup(context), BlueprintGroup<T> {

    private val _children = mutableListOf<BlueprintChild<T>>()

    protected val children: List<BlueprintChild<T>> = _children

    override fun addToViewGroup(group: ViewGroup) {
        group.addView(this)
    }

    final override fun addBlueprint(blueprint: Blueprint, constraint: T) {
        val child = BlueprintChild(blueprint, constraint)
        child.addToViewGroup(this)
        _children.add(child)
    }

    final override fun measure(available: Size, width: BlueprintSize, height: BlueprintSize): Size {
        val widthMeasureSpec = width.measureSpec(available.width)
        val heightMeasureSpec = height.measureSpec(available.height)
        measure(widthMeasureSpec, heightMeasureSpec)
        return Size(measuredWidth, measuredHeight)
    }
}


class BlueprintChild<T : LayoutConstraint>(
    val blueprint: Blueprint,
    val constraint: T,
) : Blueprint {

    override fun addToViewGroup(group: ViewGroup) {
        blueprint.addToViewGroup(group)
    }

    override fun measure(available: Size, width: BlueprintSize, height: BlueprintSize): Size {
        return blueprint.measure(available, width, height)
    }

    override fun layout(left: Int, top: Int, right: Int, bottom: Int) {
        blueprint.layout(left, top, right, bottom)
    }
}