package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.data.SourceLocation
import androidx.compose.ui.tooling.data.UiToolingDataApi
import androidx.compose.ui.unit.IntRect
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.properties.PaddingModifierParser

/**
 * @author YvesCheung
 * 2021/1/29
 */
@OptIn(UiToolingDataApi::class)
data class ComposeView(
    override val parent: Layer?,
    override val id: CharSequence,
    override val name: CharSequence,
    override val children: Sequence<Layer>,
    val bounds: IntRect,
    val modifiers: List<Modifier>,
    val location: SourceLocation?
) : Layer {

    val padding by lazy(LazyThreadSafetyMode.NONE) { PaddingModifierParser.parse(this) }

    override val width = bounds.run { right - left }

    override val height = bounds.run { bottom - top }

    override fun getLocation(): IntArray = intArrayOf(bounds.left, bounds.top)
}