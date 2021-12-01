package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import androidx.compose.ui.unit.IntRect
import com.pitaya.mobile.uinspector.hierarchy.Layer

/**
 * @author YvesCheung
 * 2021/1/29
 */
data class SubComposition(
    override val parent: Layer?,
    override val id: CharSequence,
    override val name: CharSequence,
    val bounds: IntRect,
    override val children: Sequence<Layer>,
) : Layer {

    override val width = bounds.run { right - left }

    override val height = bounds.run { bottom - top }

    override fun getLocation(): IntArray = intArrayOf(bounds.left, bounds.top)
}