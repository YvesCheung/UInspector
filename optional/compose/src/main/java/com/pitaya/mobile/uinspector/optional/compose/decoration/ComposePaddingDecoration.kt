package com.pitaya.mobile.uinspector.optional.compose.decoration

import android.graphics.Canvas
import android.graphics.Rect
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.pitaya.mobile.uinspector.ui.decoration.ViewDecoration.Companion.paddingPaint
import com.pitaya.mobile.uinspector.util.difference
import com.pitaya.mobile.uinspector.util.dpToPx
import kotlin.math.roundToInt

/**
 * @author YvesCheung
 * 2021/12/2
 */
class ComposePaddingDecoration(val view: ComposeView) : UInspectorDecoration {

    override fun draw(canvas: Canvas) {
        val location = view.getLocation()
        val viewBounds =
            Rect(location[0], location[1], location[0] + view.width, location[1] + view.height)
        val paddingBound = Rect(
            (viewBounds.left - view.padding.left.dpToPx).roundToInt(),
            (viewBounds.top - view.padding.top.dpToPx).roundToInt(),
            (viewBounds.right + view.padding.right.dpToPx).roundToInt(),
            (viewBounds.bottom + view.padding.bottom.dpToPx).roundToInt()
        )
        if (viewBounds != paddingBound) {
            canvas.difference(paddingBound, viewBounds) {
                drawRect(paddingBound, paddingPaint)
            }
        }
    }
}