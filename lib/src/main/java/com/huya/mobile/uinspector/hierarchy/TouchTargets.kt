package com.huya.mobile.uinspector.hierarchy

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.Field
import java.util.*

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

    fun findFirstTouchTarget(parent: View?): View? {
        if (parent is ViewGroup) {
            try {
                val touchTarget = firstTouchTarget.get(parent)
                if (touchTarget != null) {
                    return touchTargetChild.get(touchTarget) as? View
                }
            } catch (e: Throwable) {
                Log.e("UInspector", e.toString())
            }
        }
        return null
    }

    fun findFirstTouchTargets(parent: View?): List<View> {
        val queue = LinkedList<View>()
        var current = parent
        while (current != null) {
            queue.add(current)

            current = findFirstTouchTarget(current)
        }
        return queue
    }
}