package com.pitaya.mobile.uinspector.hierarchy

import android.app.Activity
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import com.pitaya.mobile.uinspector.util.isOnView
import com.pitaya.mobile.uinspector.util.toLocation

/**
 * @author YvesCheung
 * 2021/1/11
 */
object TouchDispatcher {

    private val windowOffset = IntArray(2)

    fun dispatchCancelEvent(event: MotionEvent, nextView: View?): Boolean {
        nextView ?: return false
        return obtainCancel(event.downTime) { cancel ->
            nextView.getLocationOnScreen(windowOffset)
            cancel.toLocation(windowOffset) {
                nextView.dispatchTouchEvent(it)
            }
        }
    }

    fun dispatchEvent(event: MotionEvent, nextView: View?): Boolean {
        nextView ?: return false
        nextView.getLocationOnScreen(windowOffset)
        return event.toLocation(windowOffset) {
            nextView.dispatchTouchEvent(it)
        }
    }

    /**
     * find the DecorView who can consume the [downEvent]
     */
    fun dispatchEventToFindDecorView(
        activity: Activity,
        downEvent: MotionEvent,
        excludeDecorView: View
    ): View? {
        obtainCancel(downEvent.downTime) { cancel ->
            val decorViews = WindowManager.findDecorViews(activity)
            for (decor in decorViews.asReversed()) {
                if (excludeDecorView === decor) {
                    continue
                }

                decor.getLocationOnScreen(windowOffset)

                var touchTargets: Layer? = null
                downEvent.toLocation(windowOffset) {
                    if (isOnView(it, decor)) {
                        decor.dispatchTouchEvent(it)
                        touchTargets = TouchTargets.findFirstTouchTarget(decor, it)
                    }
                }

                if (touchTargets != null) {
                    return decor
                } else {
                    /**
                     * if the view can't handle the [downEvent], cancel it
                     */
                    /**
                     * if the view can't handle the [downEvent], cancel it
                     */
                    cancel.toLocation(windowOffset) {
                        decor.dispatchTouchEvent(it)
                    }
                }
            }
        }
        return null
    }

    fun dispatchEventToFindTouchTargets(
        activity: Activity,
        downEvent: MotionEvent,
        excludeDecorView: View
    ): List<Layer> {
        obtainCancel(downEvent.downTime) { cancel ->
            val decorViews = WindowManager.findDecorViews(activity)
            for (decor in decorViews.asReversed()) {
                if (excludeDecorView === decor) {
                    continue
                }

                decor.getLocationOnScreen(windowOffset)

                var touchTargets = emptyList<Layer>()
                downEvent.toLocation(windowOffset) {
                    if (isOnView(it, decor)) {
                        decor.dispatchTouchEvent(it)
                        touchTargets = TouchTargets.findFirstTouchTargets(decor, it)
                    }
                }

                cancel.toLocation(windowOffset) {
                    decor.dispatchTouchEvent(it)
                }

                if (touchTargets.isNotEmpty()) {
                    return touchTargets
                }
            }
        }
        return emptyList()
    }

    private inline fun <T> obtainCancel(
        downTime: Long,
        action: (cancel: MotionEvent) -> T
    ): T {
        val cancel = MotionEvent.obtain(
            downTime,
            SystemClock.uptimeMillis(),
            MotionEvent.ACTION_CANCEL,
            0f, 0f, 0
        )
        return try {
            action(cancel)
        } finally {
            cancel.recycle()
        }
    }
}