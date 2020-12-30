package com.huya.mobile.uinspector.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent

/**
 * @author YvesCheung
 * 2020/12/29
 */
internal fun tryGetActivity(context: Context?): Activity? {
    if (context is Activity) {
        return context
    } else if (context is ContextWrapper) {
        return tryGetActivity(context.baseContext)
    }
    return null
}

internal fun isOnView(e: MotionEvent, v: View): Boolean {
    val r = Rect()
    v.getGlobalVisibleRect(r)
    return r.left <= e.x && e.x <= r.right && r.top <= e.y && e.y <= r.bottom
}

internal fun View.findRootParent(): View {
    var current: View = this
    var next: ViewParent? = current.parent
    while (next is View) {
        current = next
        next = current.parent
    }
    return current
}

internal fun log(value: String) {
    Log.d(LibName, value)
}

internal const val LibName = "UInspector"