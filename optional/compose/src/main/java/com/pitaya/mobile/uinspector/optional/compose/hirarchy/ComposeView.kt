package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import com.pitaya.mobile.uinspector.hierarchy.Layer

/**
 * @author YvesCheung
 * 2021/1/29
 */
open class ComposeView(override val parent: Layer?, /*val layoutInfo: InspectorNode*/) : Layer {

    override val name: CharSequence
        get() = TODO("Not yet implemented")
    override val id: CharSequence?
        get() = TODO("Not yet implemented")
    override val children: Sequence<Layer>
        get() = TODO("Not yet implemented")
    override val width: Int
        get() = TODO("Not yet implemented")
    override val height: Int
        get() = TODO("Not yet implemented")

    override fun getLocation(): IntArray {
        TODO("Not yet implemented")
    }

//    override val name: CharSequence get() = layoutInfo.name
//
//    override val id: CharSequence? = null
//
//    override val children: Sequence<Layer>
//        get() = layoutInfo.children.asSequence().map { ComposeView(this, it) }
//
//    override val width: Int = layoutInfo.width
//
//    override val height: Int = layoutInfo.height
//
//    override fun getLocation(): IntArray {
//        val array = IntArray(2)
//        array[0] = layoutInfo.left
//        array[1] = layoutInfo.top
//        return array
//    }
//
//    override fun hashCode(): Int = Objects.hash(parent, layoutInfo)
//
//    override fun equals(other: Any?): Boolean =
//        other is ComposeView && other.parent == parent && other.layoutInfo == layoutInfo
//
//    override fun toString(): String {
//        return "${name}(${layoutInfo.bounds})"
//    }
}