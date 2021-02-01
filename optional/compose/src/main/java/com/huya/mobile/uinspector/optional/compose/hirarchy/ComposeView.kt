package com.huya.mobile.uinspector.optional.compose.hirarchy

import com.huya.mobile.uinspector.hierarchy.AndroidView
import com.huya.mobile.uinspector.hierarchy.Layer
import com.huya.mobile.uinspector.optional.compose.inspect.ComposeLayoutInfo
import java.util.*

/**
 * @author YvesCheung
 * 2021/1/29
 */
open class ComposeView(override val parent: Layer?, val layoutInfo: ComposeLayoutInfo) : Layer {

    override val name: CharSequence get() = layoutInfo.name

    override val id: CharSequence? = null

    override val children: Sequence<Layer>
        get() = layoutInfo.children.map { ComposeView(this, it) } +
            (layoutInfo.view?.let { sequenceOf(AndroidView(it)) } ?: emptySequence())

    override val width: Int = layoutInfo.bounds.right - layoutInfo.bounds.left

    override val height: Int = layoutInfo.bounds.bottom - layoutInfo.bounds.top

    override fun getLocation(): IntArray {
        val array = IntArray(2)
        array[0] = layoutInfo.bounds.left
        array[1] = layoutInfo.bounds.top
        return array
    }

    override fun hashCode(): Int = Objects.hash(parent, layoutInfo)

    override fun equals(other: Any?): Boolean =
        other is ComposeView && other.parent == parent && other.layoutInfo == layoutInfo

    override fun toString(): String {
        return "${name}(${layoutInfo.bounds})"
    }
}