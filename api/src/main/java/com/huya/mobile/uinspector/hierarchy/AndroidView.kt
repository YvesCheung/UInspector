package com.huya.mobile.uinspector.hierarchy

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

/**
 * @author YvesCheung
 * 2021/1/29
 */
open class AndroidView(val view: View) : Layer {

    override val name: CharSequence = view::class.java.simpleName

    override val parent: Layer? get() = (view.parent as? View)?.let { AndroidView(it) }

    override val children: Sequence<Layer>
        get() = if (view is ViewGroup) view.children.map { AndroidView(it) }
        else emptySequence()

    override fun toString(): String = name.toString()
}