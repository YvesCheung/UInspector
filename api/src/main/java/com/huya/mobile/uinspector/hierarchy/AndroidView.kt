package com.huya.mobile.uinspector.hierarchy

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.huya.mobile.uinspector.util.idToString

/**
 * @author YvesCheung
 * 2021/1/29
 */
open class AndroidView(val view: View) : Layer {

    override val name: CharSequence
        get() = view::class.java.simpleName.ifBlank { view::class.java.name }

    override val id: CharSequence?
        get() = if (view.id > 0) "(${idToString(view.context, view.id)})" else null

    override val parent: Layer? get() = (view.parent as? View)?.let { AndroidView(it) }

    override val children: Sequence<Layer>
        get() = if (view is ViewGroup) view.children.map { AndroidView(it) }
        else emptySequence()

    override val width: Int get() = view.measuredWidth

    override val height: Int get() = view.measuredHeight

    override fun getLocation(): IntArray = IntArray(2).also { view.getLocationOnScreen(it) }

    override fun hashCode(): Int = view.hashCode()

    override fun equals(other: Any?): Boolean = other is AndroidView && other.view === view

    override fun toString(): String = name.toString()
}