package com.huya.mobile.uinspector.optional.compose.hirarchy

import com.huya.mobile.uinspector.hierarchy.AndroidView
import com.huya.mobile.uinspector.hierarchy.Layer
import com.huya.mobile.uinspector.optional.compose.inspect.ComposeLayoutInfo

/**
 * @author YvesCheung
 * 2021/1/29
 */
open class ComposeView(override val parent: Layer?, val layoutInfo: ComposeLayoutInfo) : Layer {

    override val name: CharSequence = layoutInfo.name

    override val children: Sequence<Layer>
        get() = layoutInfo.children.map { ComposeView(this, it) } +
            (layoutInfo.view?.let { sequenceOf(AndroidView(it)) } ?: emptySequence())

    override fun toString(): String {
        return "Compose(${layoutInfo.name}, ${layoutInfo.bounds}, ${layoutInfo.isSubcomposition})"
    }
}