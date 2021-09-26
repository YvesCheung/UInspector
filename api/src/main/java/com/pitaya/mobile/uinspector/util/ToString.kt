package com.pitaya.mobile.uinspector.util

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.view.Gravity
import android.view.View
import androidx.annotation.AnyRes
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import com.yy.mobile.whisper.IntDef
import kotlin.math.roundToInt

/**
 * @author YvesCheung
 * 2021/1/11
 */
internal val Int.dpTopx: Int
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

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
    return "0x${Integer.toHexString(hex).toUpperCase()}"
}

fun drawableToString(drawable: Drawable): CharSequence {
    return when (drawable) {
        is ColorDrawable -> colorToString(drawable.color)
        else -> drawable::class.java.simpleName
    }
}

fun resToString(context: Context, @AnyRes id: Int): String {
    return try {
        "@+${context.resources.getResourceTypeName(id)}/${context.resources.getResourceEntryName(id)}"
    } catch (e: Resources.NotFoundException) {
        hexToString(id)
    }
}

fun idToString(context: Context, @IdRes id: Int): String {
    return try {
        "@+id/${context.resources.getResourceEntryName(id)}"
    } catch (e: Resources.NotFoundException) {
        hexToString(id)
    }
}

fun visibilityToString(@IntDef(View.VISIBLE, View.INVISIBLE, View.GONE) visibility: Int): String {
    return when (visibility) {
        View.VISIBLE -> "VISIBLE"
        View.INVISIBLE -> "INVISIBLE"
        View.GONE -> "GONE"
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