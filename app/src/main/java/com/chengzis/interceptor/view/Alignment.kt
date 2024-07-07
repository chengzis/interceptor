package com.chengzis.interceptor.view

import android.util.LayoutDirection
import android.util.Size
import androidx.lifecycle.LiveData
import kotlin.math.roundToInt


fun interface Alignment {

    fun align(size: Size, space: Size, layoutDirection: Int): Offset

    fun interface Horizontal {

        fun align(size: Int, space: Int, layoutDirection: Int): Int

    }


    fun interface Vertical {
        fun align(size: Int, space: Int): Int
    }


    companion object {

        val Top: Vertical = BiasAlignment.Vertical(-1f)

        val CenterVertically: Vertical = BiasAlignment.Vertical(0f)

        val Bottom: Vertical = BiasAlignment.Vertical(1f)

        val Start: Horizontal = BiasAlignment.Horizontal(-1f)

        val CenterHorizontally: Horizontal = BiasAlignment.Horizontal(0f)

        val End: Horizontal = BiasAlignment.Horizontal(1f)

        data class DynamicHorizontal(val data:Dynamic<Horizontal>): Horizontal {
            override fun align(size: Int, space: Int, layoutDirection: Int): Int {
                return data.value.align(size, space, layoutDirection)
            }
        }
    }

}

data class BiasAlignment(
    val horizontalBias: Float,
    val verticalBias: Float
) : Alignment {
    override fun align(
        size: Size,
        space: Size,
        layoutDirection: Int
    ): Offset {
        // Convert to Px first and only round at the end, to avoid rounding twice while calculating
        // the new positions
        val centerX = (space.width - size.width).toFloat() / 2f
        val centerY = (space.height - size.height).toFloat() / 2f
        val resolvedHorizontalBias = if (layoutDirection == LayoutDirection.LTR) {
            horizontalBias
        } else {
            -1 * horizontalBias
        }

        val x = centerX * (1 + resolvedHorizontalBias)
        val y = centerY * (1 + verticalBias)
        return Offset(x.roundToInt(), y.roundToInt())
    }

    /**
     * An [Alignment.Horizontal] specified by bias: for example, a bias of -1 represents alignment
     * to the start, a bias of 0 will represent centering, and a bias of 1 will represent end.
     * Any value can be specified to obtain an alignment. Inside the [-1, 1] range, the obtained
     * alignment will position the aligned size fully inside the available space, while outside the
     * range it will the aligned size will be positioned partially or completely outside.
     *
     * @see BiasAbsoluteAlignment.Horizontal
     * @see Vertical
     */
    data class Horizontal(private val bias: Float) : Alignment.Horizontal {
        override fun align(size: Int, space: Int, layoutDirection: Int): Int {
            // Convert to Px first and only round at the end, to avoid rounding twice while
            // calculating the new positions
            val center = (space - size).toFloat() / 2f
            val resolvedBias = if (layoutDirection == LayoutDirection.LTR) bias else -1 * bias
            return (center * (1 + resolvedBias)).roundToInt()
        }
    }

    /**
     * An [Alignment.Vertical] specified by bias: for example, a bias of -1 represents alignment
     * to the top, a bias of 0 will represent centering, and a bias of 1 will represent bottom.
     * Any value can be specified to obtain an alignment. Inside the [-1, 1] range, the obtained
     * alignment will position the aligned size fully inside the available space, while outside the
     * range it will the aligned size will be positioned partially or completely outside.
     *
     * @see Horizontal
     */
    data class Vertical(private val bias: Float) : Alignment.Vertical {
        override fun align(size: Int, space: Int): Int {
            // Convert to Px first and only round at the end, to avoid rounding twice while
            // calculating the new positions
            val center = (space - size).toFloat() / 2f
            return (center * (1 + bias)).roundToInt()
        }
    }
}

