package com.huya.mobile.uinspector.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RestrictTo
import com.huya.mobile.uinspector.hierarchy.TouchTargets
import com.huya.mobile.uinspector.state.UInspectorLifecycleState
import com.huya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.huya.mobile.uinspector.ui.decoration.ViewDecoration
import com.huya.mobile.uinspector.util.log
import com.huya.mobile.uinspector.util.tryGetActivity
import com.yy.mobile.whisper.NeedError

/**
 * @author YvesCheung
 * 2020/12/29
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class UInspectorMask internal constructor(
    private val state: UInspectorLifecycleState,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @NeedError("Don't use this constructor directly!!")
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : this(UInspectorLifecycleState(tryGetActivity(context)!!), context, attrs, defStyleAttr) {
        if (!isInEditMode) {
            throw IllegalAccessException("Can't initialize a [UInspectorMask] without UInspectorLifecycleState")
        }
    }

    private val decorations: MutableList<UInspectorDecoration> = mutableListOf()

    private val gesture = GestureDetector(context,
        object : GestureDetector.SimpleOnGestureListener() {

            private var downEvent: MotionEvent? = null

            private var handle = true

            override fun onDown(e: MotionEvent): Boolean {
                downEvent?.recycle()
                downEvent = MotionEvent.obtain(e)
                return handle
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                val de = downEvent
                val activity = tryGetActivity(context)
                if (de != null && activity != null) {
                    handle = false
                    activity.dispatchTouchEvent(de)

                    val touchTargets =
                        TouchTargets.findFirstTouchTargets(activity.window.decorView)
                    updateDecoration(touchTargets)

                    val cancel = MotionEvent.obtain(
                        de.downTime,
                        System.currentTimeMillis(),
                        MotionEvent.ACTION_CANCEL,
                        0f,
                        0f,
                        0
                    )
                    activity.dispatchTouchEvent(cancel)
                    cancel.recycle()
                    handle = true
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