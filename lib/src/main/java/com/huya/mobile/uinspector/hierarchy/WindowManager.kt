package com.huya.mobile.uinspector.hierarchy

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import com.huya.mobile.uinspector.util.LibName

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

    fun findDecorViews(activity: Activity): List<View> {
        try {
            @Suppress("UNCHECKED_CAST")
            return getWindowViews.get(windowManagerGlobal) as List<View>
        } catch (e: Throwable) {
            Log.e(LibName, e.toString())
            return listOf(activity.window.decorView)
        }
    }
}