package com.pitaya.mobile.uinspector.hierarchy

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.pitaya.mobile.uinspector.util.LibName
import com.pitaya.mobile.uinspector.util.tryGetActivity
import android.view.inspector.WindowInspector

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
                if (tryGetActivity(decorView.context) === activity || //the special view add directly to windowManager
                    decorView.context is Application //the system/application layer add directly to windowManager
                ) {
                    return@filter true
                } else { //normal DecorView
                    val anyChild = (decorView as? ViewGroup)?.getChildAt(0)
                    anyChild != null &&
                        tryGetActivity(anyChild.context) === activity
                }
            }
        }
        //solution when fail
        return listOf(activity.window.decorView)
    }

    private var tryWindowInspector = true

    /**
     * Find all DecorViews from [android.view.WindowManagerGlobal]
     */
    @Suppress("KDocUnresolvedReference")
    fun findAllDecorViews(): List<View>? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && tryWindowInspector) {
            try {
                return WindowInspector.getGlobalWindowViews()
            } catch (e: Throwable) {
                tryWindowInspector = false
            }
        }
        try {
            @Suppress("UNCHECKED_CAST")
            return getWindowViews.get(windowManagerGlobal) as List<View>
        } catch (e: Throwable) {
            Log.e(LibName, e.toString())
            return null
        }
    }
}