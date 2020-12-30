package com.huya.mobile.uinspector.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RestrictTo
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.hierarchy.TouchTargets
import com.huya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.huya.mobile.uinspector.ui.decoration.ViewDecoration
import com.huya.mobile.uinspector.util.log
import com.huya.mobile.uinspector.util.tryGetActivity

/**
 * @author YvesCheung
 * 2020/12/29
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class UInspectorMask(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val decorations: MutableList<UInspectorDecoration> = mutableListOf()

    private val gesture = GestureDetector(context,
        object : GestureDetector.SimpleOnGestureListener() {

            private var downEvent: MotionEvent? = null

            override fun onDown(e: MotionEvent): Boolean {
                downEvent?.recycle()
                downEvent = MotionEvent.obtain(e)
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                val de = downEvent
                val activity = tryGetActivity(context)
                if (de != null && activity != null) {
                    val touchTargets =
                        TouchTargets.findTouchTargets(activity, de, this@UInspectorMask)
                    updateDecoration(touchTargets)
                    return true
                }
                return false
            }
        })

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return gesture.onTouchEvent(event)
    }

    private fun updateDecoration(touchViews: List<View>) {
        val dumpViews =
            touchViews.joinToString(" -> ") { view -> view::class.java.simpleName }
        log("Touch targets = $dumpViews")

        val state = UInspector.currentState.withLifecycle ?: return

        val lastTarget = state.lastTouchTarget
        if (lastTarget != null) {
            decorations.remove(ViewDecoration(lastTarget))
            state.lastTouchTarget = null
        }

        val touchTarget = touchViews.lastOrNull()
        if (touchTarget != lastTarget && touchTarget != null) {
            decorations.add(ViewDecoration(touchTarget))
            state.lastTouchTarget = touchTarget
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        decorations.forEach { it.draw(canvas) }
    }
}