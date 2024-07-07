package com.chengzis.interceptor.view

import android.content.Context
import android.graphics.Color
import android.util.Size
import android.view.View
import android.view.ViewGroup
import com.chengzis.interceptor.view.style.StyleDrawable

private class Row(
    context: Context,
    private val width: BlueprintSize,
    private val height: BlueprintSize,
    private val children: List<RowChild>,
    private val verticalAlignment: Alignment.Vertical = Alignment.Top,
    private val horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
) : ViewGroup(context), Blueprint, BlueprintView {

    private val mMeasuredSizes = mutableListOf<Size>()

    init {
        setBackgroundColor(Color.GRAY)
    }

    override fun addToBlueprintView(blueprintView: BlueprintView) {
        blueprintView.addToViewGroup(this)
        children.forEach {
            it.blueprint.addToBlueprintView(this)
        }
    }

    override fun addToViewGroup(view: View) {
        addView(view)
    }

    override fun measure(available: Size): Size {
        var widthUsed = 0
        val sizes = children.map {
            val size = it.blueprint.measure(Size(available.width - widthUsed, available.height))

            widthUsed += size.width

            size
        }
        mMeasuredSizes.clear()
        mMeasuredSizes.addAll(sizes)

        val measuredWidth = when (width) {
            is BlueprintSize.MatchParent -> available.width
            is BlueprintSize.WrapContent -> minOf(available.width, sizes.sumOf { it.width })
            is BlueprintSize.Pixel -> width.value
        }
        val measuredHeight = when (height) {
            is BlueprintSize.MatchParent -> available.height
            is BlueprintSize.WrapContent -> minOf(available.height, sizes.maxOf { it.height })
            is BlueprintSize.Pixel -> height.value
        }
        val size = Size(measuredWidth, measuredHeight)
        setMeasuredDimension(size.width, size.height)
        return size
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var widthUsed = 0

        val lefts = horizontalArrangement.arrange(
            r - l,
            mMeasuredSizes.map { it.width }.toIntArray(),
            layoutDirection
        )

        children.forEachIndexed { index, rowChild ->
            val measuredSize = mMeasuredSizes[index]

            val left = lefts[index]
            val top = t + (rowChild.verticalAlignment ?: verticalAlignment).align(
                measuredSize.height,
                b - t
            )
            val right = left + measuredSize.width
            val bottom = top + measuredSize.height

            rowChild.blueprint.layout(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
            widthUsed += measuredSize.width
        }
    }
}

private data class RowChild(
    val blueprint: Blueprint,
    val verticalAlignment: Alignment.Vertical? = null,
    val margins: Margins = Margins(0, 0, 0, 0)
)



interface RowScope : BlueprintStyle {

    fun addChild(
        blueprint: Blueprint,
        verticalAlignment: Alignment.Vertical? = null,
        margins: Margins = Margins(0, 0, 0, 0)
    )

}

fun Row(
    context: Context,
    width: BlueprintSize,
    height: BlueprintSize,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: RowScope.() -> Unit
): Blueprint {
    val children = mutableListOf<RowChild>()
    val scope = object : RowScope {
        override fun addChild(
            blueprint: Blueprint,
            verticalAlignment: Alignment.Vertical?,
            margins: Margins
        ) {
            children.add(RowChild(blueprint, verticalAlignment, margins))
        }

        override fun setBackground(background: StyleDrawable) {
            TODO("Not yet implemented")
        }
    }
    content(scope)

    return Row(
        context = context,
        width = width,
        height = height,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        children = children
    )
}



