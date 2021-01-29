package com.huya.mobile.uinspector.hierarchy

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 * @author YvesCheung
 * 2020/12/29
 */
object TouchTargets {

    val hitTest: HitTest = AndroidHitTest()

    /**
     * 1. find all decorView of current activity
     * 2. dispatch [downEvent] to the decorView and then find the touch targets by [findFirstTouchTargets]
     * 3. dispatch a cancel event to against the effect by 2.
     */
    fun findTouchTargets(
        activity: Activity,
        downEvent: MotionEvent,
        excludeDecorView: View
    ): List<Layer> {
        return TouchDispatcher.dispatchEventToFindTouchTargets(
            activity,
            downEvent,
            excludeDecorView
        )
    }

    fun findFirstTouchTargets(parent: View, touchEvent: MotionEvent): List<Layer> {
        val queue = LinkedList<Layer>()
        var current: Layer? = AndroidView(parent)
        while (current != null) {
            queue.add(current)

            current = hitTest.findNextTarget(touchEvent, current)
        }
        return queue
    }

    fun findFirstTouchTarget(parent: View, touchEvent: MotionEvent): Layer? {
        return hitTest.findNextTarget(touchEvent, AndroidView(parent))
    }
}