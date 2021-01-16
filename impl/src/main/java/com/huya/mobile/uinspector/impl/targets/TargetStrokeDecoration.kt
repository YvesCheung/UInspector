package com.huya.mobile.uinspector.impl.targets

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import com.huya.mobile.uinspector.impl.utils.dpToPx
import com.huya.mobile.uinspector.ui.decoration.UInspectorDecoration

/**
 * @author YvesCheung
 * 2021/1/15
 */
class TargetStrokeDecoration(
    private val view: View,
    @ColorInt private val representativeColor: Int
) : UInspectorDecoration {

    override fun draw(canvas: Canvas) {
        boundPaint.color = representativeColor
        val location = IntArray(2).also { view.getLocationOnScreen(it) }
        val viewBounds = Rect(
            location[0],
            location[1],
            location[0] + view.measuredWidth,
            location[1] + view.measuredHeight
        )
        canvas.drawRect(viewBounds, boundPaint)
    }

    override fun hashCode(): Int = view.hashCode()

    override fun equals(other: Any?): Boolean =
        other is TargetStrokeDecoration && view === other.view

    companion object {

        private val boundPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 2f.dpToPx
            pathEffect = DashPathEffect(floatArrayOf(2f.dpToPx, 2f.dpToPx), 0f)
        }
    }
}