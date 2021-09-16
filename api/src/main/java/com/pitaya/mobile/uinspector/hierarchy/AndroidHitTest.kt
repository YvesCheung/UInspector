package com.pitaya.mobile.uinspector.hierarchy

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.pitaya.mobile.uinspector.util.LibName
import com.pitaya.mobile.uinspector.util.isOnView
import java.lang.reflect.Field

/**
 * @author YvesCheung
 * 2021/1/29
 */
@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
open class AndroidHitTest : HitTest {

    override fun findNextTarget(event: MotionEvent, current: Layer): Layer? {
        if (current is AndroidView) {
            val view = findFirstTouchTarget(current.view, event)
            if (view != null) {
                return LayerFactoryPlugin.create(view)
            }
        }
        return null
    }

    /**
     * 1. Try to get the [view]'s 'mFirstTouchTarget' field
     * 2. If fail, use [findTouchTargetByEvent] instead
     */
    protected open fun findFirstTouchTarget(view: View?, touchEvent: MotionEvent): View? {
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
    protected open fun findTouchTargetByEvent(parent: ViewGroup, touchEvent: MotionEvent): View? {
        if (parent.childCount <= 0) return null

        val dispatchTouchOrder = ArrayList<View>(parent.childCount)
        for (drawIndex in 0 until parent.childCount) {
            val childIndex = getChildDrawingOrder(parent, drawIndex)
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
            if (child.visibility != View.GONE && isOnView(touchEvent, child)) {
                return child
            }
        }

        return null
    }

    companion object {

        protected val firstTouchTarget: Field by lazy(LazyThreadSafetyMode.NONE) {
            val f = ViewGroup::class.java.getDeclaredField("mFirstTouchTarget")
            f.isAccessible = true
            f
        }

        protected val touchTargetChild: Field by lazy(LazyThreadSafetyMode.NONE) {
            val cls = Class.forName("android.view.ViewGroup\$TouchTarget")
            val f = cls.getDeclaredField("child")
            f.isAccessible = true
            f
        }

        protected var getChildDrawingOrderNotFound = false

        /**
         * Some device can't found method [ViewGroup.getChildDrawingOrder]?
         */
        protected fun getChildDrawingOrder(parent: ViewGroup, idx: Int): Int {
            if (!getChildDrawingOrderNotFound && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    //todo: check isChildrenDrawingOrderEnabled
                    val childIndex = parent.getChildDrawingOrder(idx)
                    if (childIndex in 0 until parent.childCount) return childIndex
                } catch (e: Throwable) {
                    getChildDrawingOrderNotFound = true
                }
            }
            return idx
        }
    }
}