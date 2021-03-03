package com.pitaya.mobile.uinspector.impl.targets

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.ColorInt
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.impl.utils.dpToPx
import com.pitaya.mobile.uinspector.ui.decoration.UInspectorDecoration

/**
 * @author YvesCheung
 * 2021/1/15
 */
class TargetStrokeDecoration(
    private val layer: Layer,
    @ColorInt private val representativeColor: Int
) : UInspectorDecoration {

    override fun draw(canvas: Canvas) {
        boundPaint.color = representativeColor
        val location = layer.getLocation()
        val viewBounds = Rect(
            location[0],
            location[1],
            location[0] + layer.width,
            location[1] + layer.height
        )
        canvas.drawRect(viewBounds, boundPaint)
    }

    override fun hashCode(): Int = layer.hashCode()

    override fun equals(other: Any?): Boolean =
        other is TargetStrokeDecoration && layer === other.layer

    companion object {

        private val boundPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 2f.dpToPx
            pathEffect = DashPathEffect(floatArrayOf(2f.dpToPx, 2f.dpToPx), 0f)
        }
    }
}