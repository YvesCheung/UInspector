package com.pitaya.mobile.uinspector.hierarchy

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.github.yvescheung.whisper.NeedError
import com.pitaya.mobile.uinspector.util.idToString
import com.pitaya.mobile.uinspector.util.simpleName

/**
 * @see LayerFactoryPlugin.create
 *
 * @author YvesCheung
 * 2021/1/29
 */
open class AndroidView
@NeedError("Use LayerFactory.create(view) instead!")
constructor(val view: View) : Layer {

    override val name: CharSequence
        get() = view.simpleName

    override val id: CharSequence?
        get() = if (view.id > 0) idToString(view.context, view.id) else null

    override val parent: Layer? get() = (view.parent as? View)?.let { LayerFactoryPlugin.create(it) }

    override val children: Sequence<Layer>
        get() = if (view is ViewGroup) view.children.map { LayerFactoryPlugin.create(it) }
        else emptySequence()

    override val width: Int get() = view.measuredWidth

    override val height: Int get() = view.measuredHeight

    override fun getLocation(): IntArray = IntArray(2).also { view.getLocationOnScreen(it) }

    override fun hashCode(): Int = view.hashCode()

    override fun equals(other: Any?): Boolean = other is AndroidView && other.view === view

    override fun toString(): String = name.toString()
}