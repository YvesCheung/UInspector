package com.pitaya.mobile.uinspector.ui.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.ViewGroup
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.util.difference

/**
 * @author YvesCheung
 * 2020/12/29
 */
open class ViewDecoration(val layer: Layer) : UInspectorDecoration {

    override fun draw(canvas: Canvas) {
        val location = layer.getLocation()
        val viewBounds = Rect(
            location[0],
            location[1],
            location[0] + layer.width,
            location[1] + layer.height
        )

        if (layer is AndroidView) {
            val view = layer.view
            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let { lp ->
                val marginBounds = Rect(
                    viewBounds.left - lp.leftMargin,
                    viewBounds.top - lp.topMargin,
                    viewBounds.right + lp.rightMargin,
                    viewBounds.bottom + lp.bottomMargin
                )
                if (viewBounds != marginBounds) {
                    canvas.difference(marginBounds, viewBounds) {
                        drawRect(marginBounds, marginPaint)
                    }
                }
            }

            val paddingBound = Rect(
                viewBounds.left + view.paddingLeft,
                viewBounds.top + view.paddingTop,
                viewBounds.right - view.paddingRight,
                viewBounds.bottom - view.paddingBottom
            )
            if (viewBounds != paddingBound) {
                canvas.difference(viewBounds, paddingBound) {
                    drawRect(viewBounds, paddingPaint)
                }
                canvas.drawRect(paddingBound, boundPaint)
            } else {
                canvas.drawRect(viewBounds, boundPaint)
            }
        } else {
            canvas.drawRect(viewBounds, boundPaint)
        }
    }

    override fun hashCode(): Int = layer.hashCode()

    override fun equals(other: Any?): Boolean = other is ViewDecoration && layer === other.layer

    companion object {

        val boundPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#80FFED97")
        }

        val paddingPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#8066B3FF")
        }

        val marginPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#8093FF93")
        }
    }
}