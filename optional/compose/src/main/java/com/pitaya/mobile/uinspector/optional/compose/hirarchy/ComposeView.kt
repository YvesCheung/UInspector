package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntRect
import com.pitaya.mobile.uinspector.hierarchy.Layer

/**
 * @author YvesCheung
 * 2021/1/29
 */
open class ComposeView(
    override val parent: Layer? /*val layoutInfo: InspectorNode*/,
    override val name: CharSequence,
    override val children: Sequence<Layer>,
    val bounds: IntRect,
    val modifiers: List<Modifier>,
    val isSubComposition: Boolean
) : Layer {

    override val id: CharSequence get() = name

    override val width = bounds.run { right - left }

    override val height = bounds.run { bottom - top }

    override fun getLocation(): IntArray = intArrayOf(bounds.left, bounds.top)
}