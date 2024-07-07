package com.chengzis.interceptor.view

import android.util.LayoutDirection
import kotlin.math.roundToInt

object Arrangement {


    interface Horizontal {
        /**
         * Spacing that should be added between any two adjacent layout children.
         */
        val spacing get() = 0

        /**
         * @param sizes 所有元素的大小
         * @param layoutDirection 布局方向
         * @return 新的位置
         */
        fun arrange(
            totalSize: Int,
            sizes: IntArray,
            layoutDirection: Int
        ): IntArray
    }

    interface Vertical {
        /**
         * Spacing that should be added between any two adjacent layout children.
         */
        val spacing get() = 0

        /**
         * @param sizes 所有元素的大小
         * @return 新的位置
         */
        fun arrange(
            totalSize: Int,
            sizes: IntArray,
        ): IntArray
    }

    interface HorizontalOrVertical : Horizontal, Vertical {
        /**
         * Spacing that should be added between any two adjacent layout children.
         */
        override val spacing: Int get() = 0
    }

    val Top = object : Vertical {

        override fun arrange(totalSize: Int, sizes: IntArray): IntArray =
            placeLeftOrTop(sizes, reverseInput = false)

        override fun toString() = "Arrangement#Top"
    }

    val Bottom = object : Vertical {
        override fun arrange(totalSize: Int, sizes: IntArray): IntArray =
            placeRightOrBottom(totalSize, sizes, reverseInput = false)

        override fun toString() = "Arrangement#Bottom"
    }

    val Start = object : Horizontal {
        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
            layoutDirection: Int,
        ) = if (layoutDirection == LayoutDirection.LTR) {
            placeLeftOrTop(sizes, reverseInput = false)
        } else {
            placeRightOrBottom(totalSize, sizes, reverseInput = true)
        }

        override fun toString() = "Arrangement#Start"
    }

    val End = object : Horizontal {
        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
            layoutDirection: Int,
        ) = if (layoutDirection == LayoutDirection.LTR) {
            placeRightOrBottom(totalSize, sizes, reverseInput = false)
        } else {
            placeLeftOrTop(sizes, reverseInput = true)
        }

        override fun toString() = "Arrangement#Start"
    }

    val Center = object : HorizontalOrVertical {
        override val spacing = 0

        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
            layoutDirection: Int,
        ) = if (layoutDirection == LayoutDirection.LTR) {
            placeCenter(totalSize, sizes, reverseInput = false)
        } else {
            placeCenter(totalSize, sizes, reverseInput = true)
        }

        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
        ) = placeCenter(totalSize, sizes, reverseInput = false)

        override fun toString() = "Arrangement#Center"
    }

    val SpaceEvenly = object : HorizontalOrVertical {
        override val spacing = 0

        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
            layoutDirection: Int,
        ) = if (layoutDirection == LayoutDirection.LTR) {
            placeSpaceEvenly(totalSize, sizes, reverseInput = false)
        } else {
            placeSpaceEvenly(totalSize, sizes, reverseInput = true)
        }

        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
        ) = placeSpaceEvenly(totalSize, sizes, reverseInput = false)

        override fun toString() = "Arrangement#SpaceEvenly"
    }

    val SpaceBetween = object : HorizontalOrVertical {
        override val spacing = 0

        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
            layoutDirection: Int,
        ) = if (layoutDirection == LayoutDirection.LTR) {
            placeSpaceBetween(totalSize, sizes, reverseInput = false)
        } else {
            placeSpaceBetween(totalSize, sizes, reverseInput = true)
        }

        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
        ) = placeSpaceBetween(totalSize, sizes, reverseInput = false)

        override fun toString() = "Arrangement#SpaceBetween"
    }

    val SpaceAround = object : HorizontalOrVertical {
        override val spacing = 0

        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
            layoutDirection: Int,
        ) = if (layoutDirection == LayoutDirection.LTR) {
            placeSpaceAround(totalSize, sizes, reverseInput = false)
        } else {
            placeSpaceAround(totalSize, sizes, reverseInput = true)
        }

        override fun arrange(
            totalSize: Int,
            sizes: IntArray,
        ) = placeSpaceAround(totalSize, sizes, reverseInput = false)

        override fun toString() = "Arrangement#SpaceAround"
    }

    internal fun placeRightOrBottom(
        totalSize: Int,
        size: IntArray,
        reverseInput: Boolean
    ): IntArray {
        val outPosition = IntArray(size.size)
        val consumedSize = size.fold(0) { a, b -> a + b }
        var current = totalSize - consumedSize
        size.forEachIndexed(reverseInput) { index, it ->
            outPosition[index] = current
            current += it
        }
        return outPosition
    }

    internal fun placeLeftOrTop(size: IntArray, reverseInput: Boolean): IntArray {
        val outPosition = IntArray(size.size)
        var current = 0
        size.forEachIndexed(reverseInput) { index, it ->
            outPosition[index] = current
            current += it
        }
        return outPosition
    }

    internal fun placeCenter(
        totalSize: Int,
        size: IntArray,
        reverseInput: Boolean
    ) : IntArray {
        val outPosition = IntArray(size.size)
        val consumedSize = size.fold(0) { a, b -> a + b }
        var current = (totalSize - consumedSize).toFloat() / 2
        size.forEachIndexed(reverseInput) { index, it ->
            outPosition[index] = current.roundToInt()
            current += it.toFloat()
        }
        return outPosition
    }

    internal fun placeSpaceEvenly(
        totalSize: Int,
        size: IntArray,
        reverseInput: Boolean
    ) : IntArray {
        val outPosition = IntArray(size.size)
        val consumedSize = size.fold(0) { a, b -> a + b }
        val gapSize = (totalSize - consumedSize).toFloat() / (size.size + 1)
        var current = gapSize
        size.forEachIndexed(reverseInput) { index, it ->
            outPosition[index] = current.roundToInt()
            current += it.toFloat() + gapSize
        }
        return outPosition
    }

    internal fun placeSpaceBetween(
        totalSize: Int,
        size: IntArray,
        reverseInput: Boolean
    ) : IntArray {
        val outPosition = IntArray(size.size)
        if (size.isEmpty()) return outPosition

        val consumedSize = size.fold(0) { a, b -> a + b }
        val noOfGaps = maxOf(size.lastIndex, 1)
        val gapSize = (totalSize - consumedSize).toFloat() / noOfGaps

        var current = 0f
        if (reverseInput && size.size == 1) {
            // If the layout direction is right-to-left and there is only one gap,
            // we start current with the gap size. That forces the single item to be right-aligned.
            current = gapSize
        }
        size.forEachIndexed(reverseInput) { index, it ->
            outPosition[index] = current.roundToInt()
            current += it.toFloat() + gapSize
        }

        return outPosition
    }

    internal fun placeSpaceAround(
        totalSize: Int,
        size: IntArray,
        reverseInput: Boolean
    ) : IntArray {
        val outPosition = IntArray(size.size)
        val consumedSize = size.fold(0) { a, b -> a + b }
        val gapSize = if (size.isNotEmpty()) {
            (totalSize - consumedSize).toFloat() / size.size
        } else {
            0f
        }
        var current = gapSize / 2
        size.forEachIndexed(reverseInput) { index, it ->
            outPosition[index] = current.roundToInt()
            current += it.toFloat() + gapSize
        }

        return outPosition
    }

    private inline fun IntArray.forEachIndexed(reversed: Boolean, action: (Int, Int) -> Unit) {
        if (!reversed) {
            forEachIndexed(action)
        } else {
            for (i in (size - 1) downTo 0) {
                action(i, get(i))
            }
        }
    }

}