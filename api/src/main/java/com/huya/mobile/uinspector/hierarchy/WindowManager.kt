package com.huya.mobile.uinspector.hierarchy

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.huya.mobile.uinspector.util.LibName
import com.huya.mobile.uinspector.util.tryGetActivity

/**
 * @author YvesCheung
 * 2020/12/30
 */
@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
internal object WindowManager {

    val global by lazy(LazyThreadSafetyMode.NONE) {
        Class.forName("android.view.WindowManagerGlobal")
    }

    val windowManagerGlobal: Any by lazy(LazyThreadSafetyMode.NONE) {
        global.getDeclaredMethod("getInstance").invoke(null)
    }

    val getWindowViews by lazy(LazyThreadSafetyMode.NONE) {
        val f = global.getDeclaredField("mViews")
        f.isAccessible = true
        f
    }

    /**
     * Filter the decorView who is inside the current [activity]
     */
    fun findDecorViews(activity: Activity): List<View> {
        val allViews = findAllDecorViews()
        if (allViews != null) {
            return allViews.filter { decorView ->
                val anyChild = (decorView as? ViewGroup)?.getChildAt(0)
                anyChild != null &&
                    tryGetActivity(anyChild.context) === activity
            }
        }
        //solution when fail
        return listOf(activity.window.decorView)
    }

    /**
     * Find all DecorViews from [android.view.WindowManagerGlobal]
     */
    fun findAllDecorViews(): List<View>? {
        try {
            @Suppress("UNCHECKED_CAST")
            return getWindowViews.get(windowManagerGlobal) as List<View>
        } catch (e: Throwable) {
            Log.e(LibName, e.toString())
            return null
        }
    }
}