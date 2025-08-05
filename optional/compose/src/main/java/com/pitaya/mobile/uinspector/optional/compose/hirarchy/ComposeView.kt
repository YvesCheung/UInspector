package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import androidx.annotation.Size
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutInfo
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.tooling.data.NodeGroup
import androidx.compose.ui.tooling.data.UiToolingDataApi
import androidx.compose.ui.tooling.data.position
import androidx.compose.ui.unit.IntRect
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.inspect.CallGroupInfo
import com.pitaya.mobile.uinspector.optional.compose.inspect.SourceCodeLocation
import com.pitaya.mobile.uinspector.optional.compose.inspect.createCodeLocation
import com.pitaya.mobile.uinspector.optional.compose.inspect.parseGroupToLayer
import com.pitaya.mobile.uinspector.optional.compose.properties.PaddingModifierParser

/**
 * @author YvesCheung
 * 2021/1/29
 */
@OptIn(UiToolingDataApi::class)
class ComposeView(
    override val name: CharSequence,
    val callChain: List<CallGroupInfo>,
    private val group: NodeGroup,
    private val allSemanticsNodes: List<SemanticsNode>?,
    private val irregularChildren: Sequence<Layer>,
    parent: Layer?
) : Layer {

    override var parent: Layer? = parent
        internal set

    val bounds: IntRect = group.box

    val modifiers: List<Modifier> = group.modifierInfo.map { it.modifier }

    val padding by lazy(LazyThreadSafetyMode.NONE) { PaddingModifierParser.parse(this) }

    override val id: CharSequence? = group.position

    override val width = bounds.run { right - left }

    override val height = bounds.run { bottom - top }

    val sourceCode: SourceCodeLocation?
        get() = createCodeLocation(name, callChain.firstOrNull()?.location)

    private var androidComposeViewLocation: IntArray? = null

    private val semanticsId = (group.node as? LayoutInfo)?.semanticsId
    val semanticsNodes
        get() = allSemanticsNodes?.filter { it.id == semanticsId } ?: emptyList()

    /**
     * If DecorView is not fullscreen (such as Dialog),
     * Make an offset of the DecorView's location.
     */
    @Size(2)
    fun dialogLocationOffset(): IntArray {

        fun findDecorView(): Layer? {
            var p: Layer? = parent
            var q: Layer? = this
            while (p != null) {
                q = p
                p = p.parent
            }
            return q
        }

        fun dialogOffset(): IntArray {
            val decorView = findDecorView()
            return decorView?.getLocation() ?: intArrayOf(0, 0)
        }

        return androidComposeViewLocation
            ?: dialogOffset().also { androidComposeViewLocation = it }
    }

    @Size(2)
    override fun getLocation(): IntArray {
        val globalOffset = dialogLocationOffset()
        return intArrayOf(bounds.left + globalOffset[0], bounds.top + globalOffset[1])
    }

    var parsedChildren: Sequence<Layer>? = null
        internal set

    override val children: Sequence<Layer>
        get() = parsedChildren
            ?: run {
                val normalChildren = group.children.asSequence()
                    .flatMap { it.parseGroupToLayer(this, allSemanticsNodes) }
                normalChildren + irregularChildren
            }.also { parsedChildren = it }

    override fun toString(): String = "ComposeView(name=$name, parent=$parent)"
}