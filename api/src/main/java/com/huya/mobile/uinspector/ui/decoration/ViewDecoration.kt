package com.huya.mobile.uinspector.ui.decoration

import android.graphics.*
import android.os.Build
import android.view.ViewGroup
import com.huya.mobile.uinspector.hierarchy.AndroidView
import com.huya.mobile.uinspector.hierarchy.Layer

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

    private inline fun Canvas.difference(
        outRect: Rect,
        inRect: Rect,
        operation: Canvas.() -> Unit
    ) {
        val c = save()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                clipRect(outRect)
                clipOutRect(inRect)
                operation()
            } else {
                clipRect(outRect)
                clipRect(inRect, Region.Op.DIFFERENCE)
                operation()
            }
        } finally {
            restoreToCount(c)
        }
    }

    override fun hashCode(): Int = layer.hashCode()

    override fun equals(other: Any?): Boolean = other is ViewDecoration && layer === other.layer

    companion object {

        private val boundPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#80FFED97")
        }

        private val paddingPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#8066B3FF")
        }

        private val marginPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#8093FF93")
        }
    }
}