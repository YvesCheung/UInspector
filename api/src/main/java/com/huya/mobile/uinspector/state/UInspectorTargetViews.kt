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

    val onDraw = CopyOnWriteArrayList<() -> Unit>()

    val onDetach = CopyOnWriteArrayList<() -> Unit>()

    private val onPreDrawDispatcher = ViewTreeObserver.OnPreDrawListener {
        onDraw.forEach { it.invoke() }
        true
    }

    private val attachState = object : View.OnAttachStateChangeListener {

        override fun onViewDetachedFromWindow(v: View?) {
            onDetach.forEach { it.invoke() }
            onDestroy()
            views.clear()
        }

        override fun onViewAttachedToWindow(v: View?) {}
    }

    private val target: View? get() = views.lastOrNull()

    @UseWith("destroy")
    constructor(views: List<View>) : this(origin = views)

    init {
        target?.viewTreeObserver?.addOnPreDrawListener(onPreDrawDispatcher)
        target?.addOnAttachStateChangeListener(attachState)
    }

    internal fun onDestroy() {
        target?.removeOnAttachStateChangeListener(attachState)
        target?.viewTreeObserver?.removeOnPreDrawListener(onPreDrawDispatcher)
    }
}