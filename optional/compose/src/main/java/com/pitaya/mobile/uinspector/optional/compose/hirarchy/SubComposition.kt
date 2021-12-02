package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import androidx.compose.runtime.Composer
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.ui.tooling.data.UiToolingDataApi
import androidx.compose.ui.tooling.data.asTree
import androidx.compose.ui.unit.IntRect
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.inspect.parseGroupToLayer

/**
 * @author YvesCheung
 * 2021/1/29
 */
@OptIn(UiToolingDataApi::class)
class SubComposition(
    override val id: CharSequence?,
    val bounds: IntRect,
    private val composer: Composer
) : Layer {

    override var parent: Layer? = null
        internal set

    override var name: CharSequence = "SubComposition"
        internal set

    override val width = bounds.run { right - left }

    override val height = bounds.run { bottom - top }

    override fun getLocation(): IntArray = intArrayOf(bounds.left, bounds.top)

    @OptIn(InternalComposeApi::class)
    override val children: Sequence<Layer>
        get() = parseGroupToLayer(composer.compositionData.asTree(), this)

    override fun toString(): String = "SubComposition($name)"

}