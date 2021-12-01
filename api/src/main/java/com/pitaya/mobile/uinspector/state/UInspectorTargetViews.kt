package com.pitaya.mobile.uinspector.state

import android.view.View
import android.view.ViewTreeObserver
import com.github.yvescheung.whisper.UseWith
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.Layer
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author YvesCheung
 * 2021/1/11
 */
@Suppress("MemberVisibilityCanBePrivate")
class UInspectorTargetViews private constructor(
    origin: List<Layer>,
    private val views: MutableList<Layer> = ArrayList(origin) //copy
) : List<Layer> by views {

    @UseWith("clear")
    constructor(views: List<Layer>) : this(origin = views)

    private val onDraw = CopyOnWriteArrayList<Listener>()

    private val onDetach = CopyOnWriteArrayList<Listener>()

    private val onScroll = CopyOnWriteArrayList<Listener>()

    val target: Layer? = views.lastOrNull()

    private val onPreDrawDispatcher = ViewTreeObserver.OnPreDrawListener {
        val view = target as? AndroidView
        val parent = target?.parent as? AndroidView
        if (view == null) {
            onDraw.forEach { it.onChange() }
        } else if (view.view.isDirty || (parent != null && parent.view.isDirty)) {
            onDraw.forEach { it.onChange() }
        }
        true
    }

    private val onScrollDispatcher = ViewTreeObserver.OnScrollChangedListener {
        onScroll.forEach { it.onChange() }
    }

    private val attachState = object : View.OnAttachStateChangeListener {

        override fun onViewDetachedFromWindow(v: View?) {
            onDetach.forEach { it.onChange() }
            clear()
            views.clear()
        }

        override fun onViewAttachedToWindow(v: View?) {}
    }

    init {
        if (target is AndroidView) {
            target.view.viewTreeObserver?.addOnScrollChangedListener(onScrollDispatcher)
            target.view.viewTreeObserver?.addOnPreDrawListener(onPreDrawDispatcher)
            target.view.addOnAttachStateChangeListener(attachState)
        }
    }

    internal fun clear() {
        if (target is AndroidView) {
            target.view.removeOnAttachStateChangeListener(attachState)
            target.view.viewTreeObserver?.removeOnPreDrawListener(onPreDrawDispatcher)
            target.view.viewTreeObserver?.removeOnScrollChangedListener(onScrollDispatcher)
        }
    }

    @UseWith("removeOnDrawListener")
    fun addOnDrawListener(listener: Listener): UInspectorTargetViews {
        onDraw.add(listener)
        return this
    }

    fun removeOnDrawListener(listener: Listener): UInspectorTargetViews {
        onDraw.remove(listener)
        return this
    }

    @UseWith("removeOnDetachListener")
    fun addOnDetachListener(listener: Listener): UInspectorTargetViews {
        onDetach.add(listener)
        return this
    }

    fun removeOnDetachListener(listener: Listener): UInspectorTargetViews {
        onDetach.remove(listener)
        return this
    }

    @UseWith("removeOnScrollListener")
    fun addOnScrollListener(listener: Listener): UInspectorTargetViews {
        onScroll.add(listener)
        return this
    }

    fun removeOnScrollListener(listener: Listener): UInspectorTargetViews {
        onScroll.remove(listener)
        return this
    }

    interface Listener {

        fun onChange()
    }
}