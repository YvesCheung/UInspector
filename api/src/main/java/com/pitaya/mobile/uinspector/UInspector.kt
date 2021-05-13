package com.pitaya.mobile.uinspector

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.pitaya.mobile.uinspector.lifecycle.UInspectorLifecycle
import com.pitaya.mobile.uinspector.notification.UInspectorNotificationService
import com.pitaya.mobile.uinspector.notification.UInspectorNotificationService.Companion.PENDING_RUNNING
import com.pitaya.mobile.uinspector.plugins.UInspectorPluginService
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugins
import com.pitaya.mobile.uinspector.plugins.UInspectorPluginsImpl
import com.pitaya.mobile.uinspector.plugins.loadService
import com.pitaya.mobile.uinspector.state.UInspectorState
import com.pitaya.mobile.uinspector.ui.panel.fullscreen.UInspectorDialogFragment
import com.pitaya.mobile.uinspector.ui.panel.fullscreen.UInspectorLegacyDialogFragment
import com.pitaya.mobile.uinspector.ui.panel.fullscreen.UInspectorPanel
import com.pitaya.mobile.uinspector.ui.panel.fullscreen.UInspectorWindow
import com.pitaya.mobile.uinspector.util.log
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author YvesCheung
 * 2020/12/28
 */
//Field 'pendingInspectView' that is assigned to null immediately after the launch of service wouldn't leak the context!
@SuppressLint("StaticFieldLeak")
@Suppress("unused")
object UInspector {

    //public api -----------------------------------------------------------------------------------

    @JvmField
    val currentState = UInspectorState()

    @JvmField
    val plugins: UInspectorPlugins = UInspectorPluginsImpl()

    @JvmStatic
    @MainThread
    fun create(context: Context) {
        if (init.compareAndSet(false, true)) {
            application = context.applicationContext as Application
            installPlugins
            lifecycle.register(application)
            stop()
        }
    }

    @JvmStatic
    @MainThread
    fun destroy() {
        if (init.compareAndSet(true, false)) {
            changeStateInner(false)
            lifecycle.unRegister(application)
            application.stopService(Intent(application, UInspectorNotificationService::class.java))
        }
    }

    @JvmStatic
    @AnyThread
    fun start() = startService(true)

    @JvmStatic
    @AnyThread
    fun start(inspectView: View) {
        pendingInspectView = inspectView
        start()
    }

    @JvmStatic
    @AnyThread
    fun stop() = startService(false)

    //private impl ---------------------------------------------------------------------------------

    private lateinit var application: Application

    private val init = AtomicBoolean(false)

    private val lifecycle = UInspectorLifecycle()

    internal fun startService(running: Boolean) {
        if (!init.get()) throw IllegalStateException("UInspector has not been init")
        ContextCompat.startForegroundService(
            application,
            Intent(application, UInspectorNotificationService::class.java)
                .putExtra(PENDING_RUNNING, running)
        )
    }

    internal fun stopService() {
        if (!init.get()) throw IllegalStateException("UInspector has not been init")
        application.stopService(Intent(application, UInspectorNotificationService::class.java))
    }

    /**
     * Check the [UInspectorPanel] state to make sure the ui is correct.
     * If [UInspectorState.isRunning] is true, bring [UInspectorPanel] to front.
     */
    @MainThread
    internal fun syncState() {
        changeStateInner(currentState.isRunning)
    }

    /**
     * Unlike [changeStateTemporary], [running] will be recorded in [currentState]
     */
    @MainThread
    internal fun changeStateInner(running: Boolean) {
        changePanelState(running)
        currentState.isRunning = running
        log("change state to ${if (running) "RUNNING" else "IDLE"}")
    }

    /**
     * Change the [UInspector] state temporary until the [syncState] to be invoked
     */
    @MainThread
    internal fun changeStateTemporary(running: Boolean) {
        changePanelState(running)
        log("change state TEMPORARY to ${if (running) "RUNNING" else "IDLE"}")
    }

    private var pendingInspectView: View? = null

    private fun changePanelState(running: Boolean) {
        val currentLifecycle = currentState.withLifecycle
        if (currentLifecycle != null) { //some activity at the front end
            val activity = currentLifecycle.activity
            currentLifecycle.clear()

            if (running) {
                currentLifecycle.registerFragmentLifecycle()

                val mask: UInspectorPanel =
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                            Settings.canDrawOverlays(activity) -> UInspectorWindow()
                        activity is FragmentActivity -> UInspectorDialogFragment()
                        else -> UInspectorLegacyDialogFragment()
                    }
                currentLifecycle.panel = mask
                mask.show(activity)

                pendingInspectView?.let { mask.updateTargetView(it) }
                pendingInspectView = null
            }
        }
    }

    private val installPlugins by lazy(LazyThreadSafetyMode.NONE) {
        loadService(UInspectorPluginService::class.java).forEach {
            it.onCreate(application, plugins)
        }
    }
}