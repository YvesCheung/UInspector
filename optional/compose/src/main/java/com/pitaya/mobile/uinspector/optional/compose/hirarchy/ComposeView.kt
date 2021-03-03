package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import androidx.compose.ui.tooling.inspector.InspectorNode
import com.pitaya.mobile.uinspector.hierarchy.Layer
import java.util.*

/**
 * @author YvesCheung
 * 2021/1/29
 */
open class ComposeView(override val parent: Layer?, val layoutInfo: InspectorNode) : Layer {

    override val name: CharSequence get() = layoutInfo.name

    override val id: CharSequence? = null

    override val children: Sequence<Layer>
        get() = layoutInfo.children.asSequence().map { ComposeView(this, it) }

    override val width: Int = layoutInfo.width

    override val height: Int = layoutInfo.height

    override fun getLocation(): IntArray {
        val array = IntArray(2)
        array[0] = layoutInfo.left
        array[1] = layoutInfo.top
        return array
    }

    override fun hashCode(): Int = Objects.hash(parent, layoutInfo)

    override fun equals(other: Any?): Boolean =
        other is ComposeView && other.parent == parent && other.layoutInfo == layoutInfo

    override fun toString(): String {
        return "${name}(${layoutInfo.bounds})"
    }
}