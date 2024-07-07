package com.chengzis.interceptor.view

import android.util.Size
import android.view.ViewGroup

interface Blueprint {

    fun addToViewGroup(group: ViewGroup)

    /**
     * 测量蓝图的大小
     * @param available 可用大小
     * @return 实际大小
     */
    fun measure(available: Size, width: BlueprintSize, height: BlueprintSize): Size

    /**
     * 布局
     * @param left 左
     * @param top 上
     * @param right 右
     * @param bottom 下
     */
    fun layout(left: Int, top: Int, right: Int, bottom: Int)
}


open class LayoutConstraint(
    val width: BlueprintSize,
    val height: BlueprintSize
)

interface BlueprintGroup<T : LayoutConstraint> : Blueprint {

    fun addBlueprint(blueprint:Blueprint, constraint: T)

}
