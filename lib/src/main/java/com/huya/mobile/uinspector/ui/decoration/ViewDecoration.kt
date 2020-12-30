package com.huya.mobile.uinspector.ui.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

/**
 * @author YvesCheung
 * 2020/12/29
 */
open class ViewDecoration(val view: View) : UInspectorDecoration {

    override fun draw(canvas: Canvas) {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        canvas.drawRect(rect, boundPaint)
    }

    override fun hashCode(): Int = view.hashCode()

    override fun equals(other: Any?): Boolean = other is ViewDecoration && view === other.view

    companion object {

        private val boundPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#80ffdd00")
        }
    }
}