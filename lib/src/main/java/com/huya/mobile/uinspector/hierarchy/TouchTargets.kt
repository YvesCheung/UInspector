package com.huya.mobile.uinspector.hierarchy

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.huya.mobile.uinspector.util.LibName
import com.huya.mobile.uinspector.util.findRootParent
import com.huya.mobile.uinspector.util.isOnView
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author YvesCheung
 * 2020/12/29
 */
@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("PrivateApi")
internal object TouchTargets {

    private val firstTouchTarget: Field by lazy(LazyThreadSafetyMode.NONE) {
        val f = ViewGroup::class.java.getDeclaredField("mFirstTouchTarget")
        f.isAccessible = true
        f
    }

    private val touchTargetChild: Field by lazy(LazyThreadSafetyMode.NONE) {
        val cls = Class.forName("android.view.ViewGroup\$TouchTarget")
        val f = cls.getDeclaredField("child")
        f.isAccessible = true
        f
    }

    /**
     * 1. find all decorView of current activity
     * 2. dispatch [downEvent] to the decorView and then find the touch targets by [findFirstTouchTargets]
     * 3. dispatch a cancel event to against the effect by 2.
     */
    fun findTouchTargets(activity: Activity, downEvent: MotionEvent, exclude: View): List<View> {
        val cancel = MotionEvent.obtain(
            downEvent.downTime,
            System.currentTimeMillis(),
            MotionEvent.ACTION_CANCEL,
            0f,
            0f,
            0
        )
        try {
            val decorViews = WindowManager.findDecorViews(activity)
            val excludeRoot = exclude.findRootParent()

            for (decor in decorViews.asReversed()) {
                //todo: filter the decorView who outside the current [activity]
                if (excludeRoot === decor.findRootParent()) {
                    continue
                }

                decor.getLocationOnScreen(windowOffset)
                offsetEventToWindow(downEvent) { decor.dispatchTouchEvent(downEvent) }
                val touchTargets = findFirstTouchTargets(decor, downEvent)
                offsetEventToWindow(cancel) { decor.dispatchTouchEvent(cancel) }

                if (touchTargets.isNotEmpty()) {
                    return touchTargets
                }
            }
        } finally {
            cancel.recycle()
        }
        return emptyList()
    }

    private val windowOffset = IntArray(2)

    /**
     * Offset coordinate of the [event] by [windowOffset]
     */
    private inline fun offsetEventToWindow(
        event: MotionEvent,
        action: () -> Unit
    ) {
        val x = windowOffset[0].toFloat()
        val y = windowOffset[1].toFloat()
        event.offsetLocation(-x, -y)
        action()
        event.offsetLocation(x, y)
    }

    private fun findFirstTouchTargets(parent: View, touchEvent: MotionEvent): List<View> {
        val queue = LinkedList<View>()
        var current: View? = parent
        while (current != null) {
            queue.add(current)

            current = findFirstTouchTarget(current, touchEvent)
        }
        return queue
    }

    /**
     * 1. Try to get the [view]'s 'mFirstTouchTarget' field
     * 2. If fail, use [findTouchTargetByEvent] instead
     */
    private fun findFirstTouchTarget(view: View?, touchEvent: MotionEvent): View? {
        if (view is ViewGroup) {
            return try {
                val touchTarget = firstTouchTarget.get(view)
                if (touchTarget != null) {
                    touchTargetChild.get(touchTarget) as? View
                } else {
                    findTouchTargetByEvent(view, touchEvent)
                }
            } catch (e: Throwable) {
                Log.e(LibName, e.toString())
                findTouchTargetByEvent(view, touchEvent)
            }
        }
        return null
    }

    /**
     * 1. Sort the children in [parent] by the [ViewGroup.getChildDrawingOrder] and [View.getZ]
     * 2. Find the child who is on the top and is able to receive the [touchEvent]
     */
    fun findTouchTargetByEvent(parent: ViewGroup, touchEvent: MotionEvent): View? {
        if (parent.childCount <= 0) return null

        val dispatchTouchOrder = ArrayList<View>(parent.childCount)
        for (drawIndex in 0 until parent.childCount) {

            //todo: check isChildrenDrawingOrderEnabled
            var childIndex = parent.getChildDrawingOrder(drawIndex)
            if (childIndex !in 0 until parent.childCount) childIndex = drawIndex

            val child = parent.getChildAt(childIndex)

            var insertIndex: Int = drawIndex
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // insert ahead of any Views with greater Z
                val currentZ = child.z
                while (insertIndex > 0 && dispatchTouchOrder[insertIndex - 1].z > currentZ) {
                    insertIndex--
                }
            }
            dispatchTouchOrder.add(insertIndex, child)
        }

        for (child in dispatchTouchOrder.asReversed()) { //from top to bottom
            if (isOnView(touchEvent, child)) {
                return child
            }
        }

        return null
    }
}