package com.huya.mobile.uinspector.lifecycle

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.app.Fragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.state.UInspectorLifecycleState
import com.huya.mobile.uinspector.ui.panel.fullscreen.UInspectorDialogFragment
import com.huya.mobile.uinspector.ui.panel.fullscreen.UInspectorLegacyDialogFragment
import com.huya.mobile.uinspector.util.log
import com.yy.mobile.whisper.NotThreadSafe
import com.yy.mobile.whisper.UseWith

@Suppress("MemberVisibilityCanBePrivate")
@NotThreadSafe
internal class UInspectorLifecycle {

    var currentActivity: Activity? = null
        private set

    @UseWith("unRegister")
    fun register(application: Application) {
        application.registerActivityLifecycleCallbacks(activityLifecycle)
    }

    fun unRegister(application: Application) {
        application.registerActivityLifecycleCallbacks(activityLifecycle)
    }

    private val activityLifecycle = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            onActivityChanged(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            onActivityChanged(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (currentActivity === activity) {
                onActivityChanged(null)
            }
        }

        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    }

    @MainThread
    private fun onActivityChanged(newActivity: Activity?) {
        currentActivity = newActivity
        val lastActivity = UInspector.currentState.withLifecycle?.activity
        if (lastActivity !== newActivity) {
            if (lastActivity != null) {
                UInspector.currentState.withLifecycle?.panel?.close()
                UInspector.currentState.withLifecycle = null
            }
            if (newActivity != null) {
                UInspector.currentState.withLifecycle = UInspectorLifecycleState(newActivity)

                if (newActivity is FragmentActivity) {
                    newActivity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                        object : FragmentManager.FragmentLifecycleCallbacks() {

                            override fun onFragmentAttached(
                                fm: FragmentManager,
                                f: androidx.fragment.app.Fragment,
                                context: Context
                            ) {
                                log("onFragmentAttached $f")
                                if (f !is UInspectorDialogFragment) {
                                    UInspector.makeSureState()
                                }
                            }
                        }, true
                    )
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    newActivity.fragmentManager
                        .registerFragmentLifecycleCallbacks(
                            object : android.app.FragmentManager.FragmentLifecycleCallbacks() {

                                override fun onFragmentAttached(
                                    fm: android.app.FragmentManager?,
                                    f: Fragment?,
                                    context: Context?
                                ) {
                                    log("onFragmentAttached $f")
                                    if (f !is UInspectorLegacyDialogFragment) {
                                        UInspector.makeSureState()
                                    }
                                }
                            },
                            true
                        )
                }
                UInspector.makeSureState()
            }
        }
    }
}