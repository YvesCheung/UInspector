package com.huya.mobile.uinspector.hierarchy

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.huya.mobile.uinspector.util.LibName
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

    val firstTouchTarget: Field by lazy(LazyThreadSafetyMode.NONE) {
        val f = ViewGroup::class.java.getDeclaredField("mFirstTouchTarget")
        f.isAccessible = true
        f
    }

    val touchTargetChild: Field by lazy(LazyThreadSafetyMode.NONE) {
        val cls = Class.forName("android.view.ViewGroup\$TouchTarget")
        val f = cls.getDeclaredField("child")
        f.isAccessible = true
        f
    }

    fun findFirstTouchTargets(parent: View, touchEvent: MotionEvent): List<View> {
        val queue = LinkedList<View>()
        var current: View? = parent
        while (current != null) {
            queue.add(current)

            current = findFirstTouchTarget(current, touchEvent)
        }
        return queue
    }

    fun findFirstTouchTarget(parent: View?, touchEvent: MotionEvent): View? {
        if (parent is ViewGroup) {
            return try {
                val touchTarget = firstTouchTarget.get(parent)
                if (touchTarget != null) {
                    touchTargetChild.get(touchTarget) as? View
                } else {
                    findTouchTargetByEvent(parent, touchEvent)
                }
            } catch (e: Throwable) {
                Log.e(LibName, e.toString())
                findTouchTargetByEvent(parent, touchEvent)
            }
        }
        return null
    }

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