package com.pitaya.mobile.uinspector.state

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.ui.panel.fullscreen.UInspectorDialogFragment
import com.pitaya.mobile.uinspector.ui.panel.fullscreen.UInspectorLegacyDialogFragment
import com.pitaya.mobile.uinspector.ui.panel.fullscreen.UInspectorPanel
import com.pitaya.mobile.uinspector.util.log
import com.github.yvescheung.whisper.NotThreadSafe
import com.github.yvescheung.whisper.UseWith

/**
 * Store the information which is bound to the lifecycle of [activity]
 *
 * @author YvesCheung
 * 2020/12/30
 */
@NotThreadSafe
class UInspectorLifecycleState @UseWith("clear") constructor(val activity: Activity) {

    var panel: UInspectorPanel? = null
        internal set

    var lastTargetViews: UInspectorTargetViews? = null
        internal set

    private var supportFragmentLifecycle: FragmentManager.FragmentLifecycleCallbacks? = null

    //NoClassDefFound android.app.FragmentManager.FragmentLifecycleCallbacks below Build.VERSION_CODES.O
    private var fragmentLifecycle: Any? = null

    internal fun clear() {
        unRegisterFragmentLifecycle()
        panel?.close()
        panel = null
        lastTargetViews?.clear()
        lastTargetViews = null
    }

    internal fun registerFragmentLifecycle() {
        if (activity is FragmentActivity) {
            val lifecycle = object : FragmentManager.FragmentLifecycleCallbacks() {

                override fun onFragmentAttached(
                    fm: FragmentManager,
                    f: Fragment,
                    context: Context
                ) {
                    log("onFragmentAttached $f")
                    if (f is DialogFragment && f !is UInspectorDialogFragment) {
                        UInspector.syncState()
                    }
                }
            }.also { supportFragmentLifecycle = it }

            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(lifecycle, true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val lifecycle = object : android.app.FragmentManager.FragmentLifecycleCallbacks() {

                override fun onFragmentAttached(
                    fm: android.app.FragmentManager?,
                    f: android.app.Fragment?,
                    context: Context?
                ) {
                    log("onFragmentAttached $f")
                    if (f is android.app.DialogFragment && f !is UInspectorLegacyDialogFragment) {
                        UInspector.syncState()
                    }
                }
            }.also { fragmentLifecycle = it }

            activity.fragmentManager.registerFragmentLifecycleCallbacks(lifecycle, true)
        }
    }

    internal fun unRegisterFragmentLifecycle() {
        if (activity is FragmentActivity) {
            supportFragmentLifecycle?.let {
                activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(it)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (fragmentLifecycle as? android.app.FragmentManager.FragmentLifecycleCallbacks)?.let {
                activity.fragmentManager.unregisterFragmentLifecycleCallbacks(it)
            }
        }
    }
}