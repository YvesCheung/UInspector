package com.huya.mobile.uinspector.state

import android.view.View
import android.view.ViewTreeObserver
import com.yy.mobile.whisper.UseWith
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author YvesCheung
 * 2021/1/11
 */
@Suppress("MemberVisibilityCanBePrivate")
class UInspectorTargetViews private constructor(
    origin: List<View>,
    private val views: MutableList<View> = ArrayList(origin) //copy
) : List<View> by views {

    @UseWith("clear")
    constructor(views: List<View>) : this(origin = views)

    private val onDraw = CopyOnWriteArrayList<Listener>()

    private val onDetach = CopyOnWriteArrayList<Listener>()

    private val target = views.lastOrNull()

    private val onPreDrawDispatcher = ViewTreeObserver.OnPreDrawListener {
        val view = target
        val parent = target?.parent as? View
        if ((view != null && view.isDirty) || (parent != null && parent.isDirty)) {
            onDraw.forEach { it.onChange() }
        }
        true
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
        target?.viewTreeObserver?.addOnPreDrawListener(onPreDrawDispatcher)
        target?.addOnAttachStateChangeListener(attachState)
    }

    internal fun clear() {
        target?.removeOnAttachStateChangeListener(attachState)
        target?.viewTreeObserver?.removeOnPreDrawListener(onPreDrawDispatcher)
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

    interface Listener {

        fun onChange()
    }
}