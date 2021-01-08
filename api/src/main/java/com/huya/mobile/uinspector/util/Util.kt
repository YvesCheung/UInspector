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

internal val Int.pxToDp: Int
    get() = (this.toFloat() / Resources.getSystem().displayMetrics.density).toInt()

internal val Number.pxToDp: Float
    get() = this.toFloat() / Resources.getSystem().displayMetrics.density

internal val Int.dpStr: String
    get() = "${pxToDp}dp"

val Number.dpStr: String
    get() = "${pxToDp.roundToInt()}dp"

val Int.pxToSp: Int
    get() = (this.toFloat() / Resources.getSystem().displayMetrics.scaledDensity).toInt()

internal val Number.pxToSp: Float
    get() = this.toFloat() / Resources.getSystem().displayMetrics.scaledDensity

val Int.spStr: String
    get() = "${pxToSp}sp"

val Number.spStr: String
    get() = "${pxToSp.roundToInt()}sp"

/**
 * todo: parse the state
 */
fun colorToString(color: ColorStateList): CharSequence {
    return colorToString(color.defaultColor)
}

fun colorToString(@ColorInt color: Int): CharSequence {
    val str = SpannableStringBuilder(hexToString(color)).append(" ")
    val start = str.length
    str.append("    ")
    val end = str.length
    str.setSpan(BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return str
}

fun hexToString(hex: Int): String {
    return "0x${Integer.toHexString(hex)}"
}

fun drawableToString(drawable: Drawable): CharSequence {
    return when (drawable) {
        is ColorDrawable -> colorToString(drawable.color)
        else -> drawable::class.java.simpleName
    }
}

fun idToString(context: Context, @IdRes id: Int): String {
    return try {
        "@+id/${context.resources.getResourceEntryName(id)}"
    } catch (e: Resources.NotFoundException) {
        hexToString(id)
    }
}

fun visibilityToString(@IntDef(VISIBLE, INVISIBLE, GONE) visibility: Int): String {
    return when (visibility) {
        VISIBLE -> "VISIBLE"
        INVISIBLE -> "INVISIBLE"
        GONE -> "GONE"
        else -> throw IllegalArgumentException("What's $visibility?")
    }
}

fun gravityToString(gravity: Int): String {
    val result = StringBuilder()
    if (gravity and Gravity.FILL == Gravity.FILL) {
        result.append("FILL").append(' ')
    } else {
        if (gravity and Gravity.FILL_VERTICAL == Gravity.FILL_VERTICAL) {
            result.append("FILL_VERTICAL").append(' ')
        } else {
            if (gravity and Gravity.TOP == Gravity.TOP) {
                result.append("TOP").append(' ')
            }
            if (gravity and Gravity.BOTTOM == Gravity.BOTTOM) {
                result.append("BOTTOM").append(' ')
            }
        }
        if (gravity and Gravity.FILL_HORIZONTAL == Gravity.FILL_HORIZONTAL) {
            result.append("FILL_HORIZONTAL").append(' ')
        } else {
            if (gravity and Gravity.START == Gravity.START) {
                result.append("START").append(' ')
            } else if (gravity and Gravity.LEFT == Gravity.LEFT) {
                result.append("LEFT").append(' ')
            }
            if (gravity and Gravity.END == Gravity.END) {
                result.append("END").append(' ')
            } else if (gravity and Gravity.RIGHT == Gravity.RIGHT) {
                result.append("RIGHT").append(' ')
            }
        }
    }
    if (gravity and Gravity.CENTER == Gravity.CENTER) {
        result.append("CENTER").append(' ')
    } else {
        if (gravity and Gravity.CENTER_VERTICAL == Gravity.CENTER_VERTICAL) {
            result.append("CENTER_VERTICAL").append(' ')
        }
        if (gravity and Gravity.CENTER_HORIZONTAL == Gravity.CENTER_HORIZONTAL) {
            result.append("CENTER_HORIZONTAL").append(' ')
        }
    }
    if (result.isEmpty()) {
        result.append("NO GRAVITY").append(' ')
    }
    if (gravity and Gravity.DISPLAY_CLIP_VERTICAL == Gravity.DISPLAY_CLIP_VERTICAL) {
        result.append("DISPLAY_CLIP_VERTICAL").append(' ')
    }
    if (gravity and Gravity.DISPLAY_CLIP_HORIZONTAL == Gravity.DISPLAY_CLIP_HORIZONTAL) {
        result.append("DISPLAY_CLIP_HORIZONTAL").append(' ')
    }
    result.deleteCharAt(result.length - 1)
    return result.toString()
}

fun CharSequence?.quote(): CharSequence? {
    return if (this != null) "\"$this\"" else this
}

internal fun log(value: String) {
    Log.d(LibName, value)
}

internal const val LibName = "UInspector"