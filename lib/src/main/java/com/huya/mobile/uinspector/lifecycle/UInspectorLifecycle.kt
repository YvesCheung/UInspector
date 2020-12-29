package com.huya.mobile.uinspector.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.annotation.MainThread
import com.yy.mobile.whisper.NotThreadSafe

@Suppress("MemberVisibilityCanBePrivate")
@NotThreadSafe
internal class UInspectorLifecycle : ActivityLifecycleCallbacks {

    var currentActivity: Activity? = null
        private set

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        setActivity(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        setActivity(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity === activity) currentActivity = null
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    private var pendingAction: ((Activity) -> Unit)? = null

    @MainThread
    fun runInActivity(action: (Activity) -> Unit) {
        pendingAction = null

        val activity = currentActivity
        if (activity != null) {
            action(activity)
        } else {
            pendingAction = action
        }
    }

    @MainThread
    private fun setActivity(activity: Activity) {
        currentActivity = activity
        pendingAction?.invoke(activity)
        pendingAction = null
    }
}