package com.huya.mobile.uinspector

import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS
import com.huya.mobile.uinspector.lifecycle.UInspectorLifecycle
import com.huya.mobile.uinspector.service.UInspectorService
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @author YvesCheung
 * 2020/12/28
 */
object UInspector {

    private val init = AtomicBoolean(false)

    private val lifecycle =
        UInspectorLifecycle()

    @JvmStatic
    fun init(context: Context) {
        if (init.compareAndSet(false, true)) {
            (context.applicationContext as Application).registerActivityLifecycleCallbacks(lifecycle)
        }
    }


    fun isAccessibilitySettingsOn(
        context: Context
    ): Boolean {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices =
            activityManager.getRunningServices(100) // 获取正在运行的服务列表
        if (runningServices.size < 0) {
            return false
        }
        for (i in runningServices.indices) {
            val service = runningServices[i].service
            if (service.className == UInspectorService::class.java.name) {
                return true
            }
        }
        return false
    }

    fun toSettingActivity(context: Context) {
        val intent = Intent(ACTION_ACCESSIBILITY_SETTINGS)
            .putExtra(
                "android.intent.extra.COMPONENT_NAME",
                ComponentName("com.huya.mobile.uinspector.service", "UInspectorService")
            )
        context.startActivity(intent)
    }
}