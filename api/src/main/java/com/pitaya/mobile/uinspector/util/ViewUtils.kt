package com.pitaya.mobile.uinspector.util

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent

/**
 * @author YvesCheung
 * 2021/1/11
 */
/**
 * @return Find the DecorView of this [View]
 */
internal fun View.findRootParent(): View {
    var current: View = this
    var next: ViewParent? = current.parent
    while (next is View) {
        current = next
        next = current.parent
    }
    return current
}

/**
 * @param e Already offset to the current window!!
 * @param v View in the current window
 *
 * @return whether the [e] can be dispatched to [v]
 */
internal fun isOnView(e: MotionEvent, v: View): Boolean {
    val r = Rect()
    v.getGlobalVisibleRect(r)
    return r.left <= e.x && e.x <= r.right && r.top <= e.y && e.y <= r.bottom
}