@file:Suppress("unused")

package com.pitaya.mobile.uinspector.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.Size

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

/**
 * Offset coordinate of the [MotionEvent] by [offset]
 * @see toLocation
 */
internal inline fun <T> MotionEvent.fromLocation(
    @Size(2) offset: IntArray,
    action: (MotionEvent) -> T
): T {
    val x = offset[0].toFloat()
    val y = offset[1].toFloat()
    this.offsetLocation(x, y)
    return try {
        action(this)
    } finally {
        this.offsetLocation(-x, -y)
    }
}

/**
 * Offset coordinate of the [MotionEvent] by [offset]
 * @see fromLocation
 */
internal inline fun <T> MotionEvent.toLocation(
    @Size(2) offset: IntArray,
    action: (MotionEvent) -> T
): T {
    val x = offset[0].toFloat()
    val y = offset[1].toFloat()
    this.offsetLocation(-x, -y)
    return try {
        action(this)
    } finally {
        this.offsetLocation(x, y)
    }
}


internal fun log(value: String) {
    Log.d(LibName, value)
}

internal const val LibName = "UInspector"