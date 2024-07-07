package com.chengzis.interceptor.view

import android.util.Size
import android.view.View


/**
 * 大小模式
 */
sealed interface BlueprintSize {

    object MatchParent : BlueprintSize

    object WrapContent : BlueprintSize

    data class Pixel(val value: Int) : BlueprintSize

    data class DynamicSize(val data: Dynamic<BlueprintSize>) : BlueprintSize
}

/**
 * 转换测量模式
 */
fun BlueprintSize.measureSpec(available: Int): Int = when (this) {
    BlueprintSize.MatchParent -> View.MeasureSpec.makeMeasureSpec(
        available,
        View.MeasureSpec.EXACTLY
    )

    BlueprintSize.WrapContent -> View.MeasureSpec.makeMeasureSpec(
        available,
        View.MeasureSpec.AT_MOST
    )

    is BlueprintSize.Pixel -> View.MeasureSpec.makeMeasureSpec(value, View.MeasureSpec.EXACTLY)

    is BlueprintSize.DynamicSize -> data.value.measureSpec(available)
}

fun getAvailableSize(widthMeasureSpec: Int, heightMeasureSpec: Int) : Size {
    return Size(
        View.MeasureSpec.getSize(widthMeasureSpec),
        View.MeasureSpec.getSize(heightMeasureSpec)
    )
}