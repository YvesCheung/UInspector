package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.data.Group
import androidx.compose.ui.tooling.data.SourceLocation
import androidx.compose.ui.tooling.data.UiToolingDataApi
import androidx.compose.ui.tooling.data.position
import androidx.compose.ui.unit.IntRect
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.inspect.androidViewChildren
import com.pitaya.mobile.uinspector.optional.compose.inspect.parseGroupToLayer
import com.pitaya.mobile.uinspector.optional.compose.inspect.subComposedChildren
import com.pitaya.mobile.uinspector.optional.compose.properties.PaddingModifierParser

/**
 * @author YvesCheung
 * 2021/1/29
 */
@OptIn(UiToolingDataApi::class)
data class ComposeView(
    override val name: CharSequence,
    override val parent: Layer?,
    private val group: Group,
    private val childrenFixed: Sequence<Layer>? = null
) : Layer {

    val bounds: IntRect = group.box

    val modifiers: List<Modifier> = group.modifierInfo.map { it.modifier }

    val location: SourceLocation? = group.location

    val padding by lazy(LazyThreadSafetyMode.NONE) { PaddingModifierParser.parse(this) }

    override val id: CharSequence? = group.position

    override val width = bounds.run { right - left }

    override val height = bounds.run { bottom - top }

    override fun getLocation(): IntArray = intArrayOf(bounds.left, bounds.top)

    override val children: Sequence<Layer>
        get() = childrenFixed
            ?: group.children.asSequence().flatMap { parseGroupToLayer(it, this) } +
            group.subComposedChildren(this, name) + group.androidViewChildren()
}