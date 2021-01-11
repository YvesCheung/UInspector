@file:Suppress("unused")

package com.huya.mobile.uinspector.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewParent
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.annotation.Size
import com.yy.mobile.whisper.IntDef
import kotlin.math.roundToInt

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