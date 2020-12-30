package com.huya.mobile.uinspector.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View

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

internal fun reviseToView(v: View, event: MotionEvent) {
    val location = IntArray(2)
    v.getLocationOnScreen(location)
    event.offsetLocation((-location[0]).toFloat(), (-location[1]).toFloat())
}

internal fun isOnView(e: MotionEvent, v: View): Boolean {
    val r = Rect()
    v.getGlobalVisibleRect(r)
    return r.left <= e.rawX && e.rawX <= r.right && r.top <= e.rawY && e.rawY <= r.bottom
}

internal fun log(value: String) {
    Log.d(LibName, value)
}

internal const val LibName = "UInspector"