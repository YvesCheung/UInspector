package com.pitaya.mobile.uinspector.util

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.pitaya.mobile.uinspector.R
import com.pitaya.mobile.uinspector.UInspector

/**
 * @author YvesCheung
 * 2021/1/11
 */
inline fun SpannableStringBuilder.newLine(
    indentSize: Int,
    write: SpannableStringBuilder.() -> Unit
) {
    var size = indentSize
    while (size-- > 0) {
        append(" ")
    }
    append("- ")
    write()
    append("\n")
}

inline fun SpannableStringBuilder.withColor(
    context: Context,
    @ColorRes color: Int = R.color.uinspector_primary_color,
    write: SpannableStringBuilder.() -> Unit
) {
    val start = length
    write()
    val end = length
    setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, color)),
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

inline fun SpannableStringBuilder.withBold(
    write: SpannableStringBuilder.() -> Unit
) {
    val start = length
    write()
    val end = length
    setSpan(
        TextAppearanceSpan(null, Typeface.BOLD, -1, null, null),
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

inline fun link(
    content: CharSequence,
    crossinline onClick: (View) -> Unit
): CharSequence {
    val str = SpannableString(content)
    str.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick(widget)
        }
    }, 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return str
}

inline fun linkToView(
    content: CharSequence,
    crossinline toView: () -> View?
): CharSequence {
    return link(content) {
        val newTarget = toView()
        if (newTarget != null) {
            UInspector.currentState.withLifecycle?.panel?.updateTargetView(newTarget)
        }
    }
}