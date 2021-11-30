package com.pitaya.mobile.uinspector.lifecycle

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.state.UInspectorLifecycleState
import com.pitaya.mobile.uinspector.util.log
import com.github.yvescheung.whisper.NotThreadSafe
import com.github.yvescheung.whisper.UseWith


@Suppress("MemberVisibilityCanBePrivate")
@NotThreadSafe
internal class UInspectorLifecycle {

    var currentActivity: Activity? = null
        private set

    @UseWith("unRegister")
    fun register(application: Application) {
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycle)
        application.registerActivityLifecycleCallbacks(activityLifecycle)
    }

    fun unRegister(application: Application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycle)
        ProcessLifecycleOwner.get().lifecycle.removeObserver(appLifecycle)
    }

    private val appLifecycle = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onForeground() {
            val running = UInspector.currentState.isRunning
            log("onForeground: restart service, isRunning = $running")
            UInspector.startService(running)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onBackground() {
            log("onBackground: stop service")
            UInspector.changeStateTemporary(false)
            UInspector.stopService()
        }
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
                UInspector.currentState.withLifecycle?.clear()
                UInspector.currentState.withLifecycle = null
            }
            if (newActivity != null) {
                UInspector.currentState.withLifecycle = UInspectorLifecycleState(newActivity)
                UInspector.syncState()
            }
        }
    }
}