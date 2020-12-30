package com.huya.mobile.uinspector.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.annotation.MainThread
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.state.UInspectorLifecycleState
import com.yy.mobile.whisper.NotThreadSafe

@Suppress("MemberVisibilityCanBePrivate")
@NotThreadSafe
internal class UInspectorLifecycle : ActivityLifecycleCallbacks {

    var currentActivity: Activity? = null
        private set

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

    @MainThread
    private fun onActivityChanged(newActivity: Activity?) {
        currentActivity = newActivity
        val lastActivity = UInspector.currentState.withLifecycle?.activity
        if (lastActivity !== newActivity) {
            if (newActivity != null) {
                UInspector.currentState.withLifecycle = UInspectorLifecycleState(newActivity)
            } else {
                UInspector.currentState.withLifecycle = null
            }
        }
        UInspector.changeStateInner(UInspector.currentState.isRunning)
    }
}