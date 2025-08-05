package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import androidx.compose.runtime.Composer
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.tooling.data.UiToolingDataApi
import androidx.compose.ui.tooling.data.asTree
import androidx.compose.ui.unit.IntRect
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.inspect.CallGroupInfo
import com.pitaya.mobile.uinspector.optional.compose.inspect.parseGroupToLayer

/**
 * @author YvesCheung
 * 2021/1/29
 */
@OptIn(UiToolingDataApi::class)
class SubComposition(
    override val id: CharSequence?,
    override var name: CharSequence,
    val callChain: List<CallGroupInfo>,
    val bounds: IntRect,
    private val subComposer: Composer,
    private val semanticsNodes: List<SemanticsNode>?
) : Layer {

    override var parent: Layer? = null
        internal set

    override val width = bounds.run { right - left }

    override val height = bounds.run { bottom - top }

    override fun getLocation(): IntArray = intArrayOf(bounds.left, bounds.top)

    override val children: Sequence<Layer>
        get() = subComposer.compositionData.asTree()
            .parseGroupToLayer(this, semanticsNodes = semanticsNodes)

    override fun toString(): String = "SubComposition($name)"

}