package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import androidx.annotation.Size
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.data.Group
import androidx.compose.ui.tooling.data.UiToolingDataApi
import androidx.compose.ui.tooling.data.position
import androidx.compose.ui.unit.IntRect
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.inspect.SourceCodeLocation
import com.pitaya.mobile.uinspector.optional.compose.inspect.androidViewChildren
import com.pitaya.mobile.uinspector.optional.compose.inspect.parseGroupToLayer
import com.pitaya.mobile.uinspector.optional.compose.inspect.subComposedChildren
import com.pitaya.mobile.uinspector.optional.compose.properties.PaddingModifierParser
import com.pitaya.mobile.uinspector.util.simpleName

/**
 * @author YvesCheung
 * 2021/1/29
 */
@OptIn(UiToolingDataApi::class)
class ComposeView(
    override val name: CharSequence,
    private val group: Group,
    val sourceCode: SourceCodeLocation?,
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

    private var androidComposeViewLocation: IntArray? = null

    /**
     * 如果ComposeView是在Dialog上，[bounds]会缺少[AndroidComposeView]本身的偏移。
     * 但如果不在Dialog上就不会，连状态栏的偏移都包括在内了，不知道为什么。
     */
    @Size(2)
    fun dialogLocationOffset(): IntArray {

        fun findAndroidComposeView(): Layer? {
            var p = parent
            while (p != null) {
                if (p is AndroidComposeView) return p
                if (p is AndroidView) break
                p = p.parent
            }
            return null
        }

        fun dialogOffset(): IntArray {
            val composeView = findAndroidComposeView()
            if (composeView != null) {
                val container = composeView.parent
                if (container != null && container.name == "DialogLayout") {
                    return composeView.getLocation()
                }
            }
            //不在Dialog上
            return intArrayOf(0, 0)
        }

        return androidComposeViewLocation
            ?: dialogOffset().also { androidComposeViewLocation = it }
    }

    @Size(2)
    override fun getLocation(): IntArray {
        val globalOffset = dialogLocationOffset()
        return intArrayOf(bounds.left + globalOffset[0], bounds.top + globalOffset[1])
    }

    internal var logicalChildren: Sequence<Layer> = emptySequence()

    override val children: Sequence<Layer>
        get() = logicalChildren +
            group.children.asSequence().flatMap { parseGroupToLayer(it, this) } +
            group.subComposedChildren() +
            group.androidViewChildren()

    override fun toString(): String = "ComposeView(name=$name, parent=$parent)"
}