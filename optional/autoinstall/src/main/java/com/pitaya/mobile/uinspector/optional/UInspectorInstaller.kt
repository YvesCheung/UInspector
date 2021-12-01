package com.pitaya.mobile.uinspector.optional

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process.myPid
import androidx.annotation.RestrictTo
import com.pitaya.mobile.uinspector.UInspector
import androidx.startup.Initializer


/**
 * Auto install the [UInspector].
 *
 * You can disable this feature by excluding this module in `build.gradle`:
 *
 * ```groovy
 * dependencies {
 *     debugImplementation('com.pitaya.mobile:Uinspector:x.y.z') {
 *         exclude module: 'Uinspector-optional-autoinstall'
 *     }
 * }
 * ```
 *
 * @since 1.0.9
 * @author YvesCheung
 * 2020/12/28
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class UInspectorInstaller : Initializer<UInspector> {

    override fun create(context: Context): UInspector {
        val processName = getCurrentProcessName(context)
        if (processName == null /*unknown?*/ || processName == context.packageName /*main process*/) {
            UInspector.create(context)
        }
        return UInspector
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    /**
     * @return 当前进程名
     */
    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    private fun getCurrentProcessName(context: Context): String? {

        fun getCurrentProcessNameByActivityThread(): String? {
            var processName: String? = null
            try {
                val declaredMethod = Class.forName(
                    "android.app.ActivityThread",
                    false,
                    Application::class.java.classLoader
                ).getDeclaredMethod("currentProcessName")
                declaredMethod.isAccessible = true
                processName = declaredMethod.invoke(null) as String
            } catch (e: Throwable) {
            }
            return processName
        }

        fun getCurrentProcessNameByActivityManager(context: Context): String? {
            val pid: Int = myPid()
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            if (am != null) {
                val runningAppList = am.runningAppProcesses
                if (runningAppList != null) {
                    for (processInfo in runningAppList) {
                        if (processInfo.pid == pid) {
                            return processInfo.processName
                        }
                    }
                }
            }
            return null
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Application.getProcessName()
        } else {
            getCurrentProcessNameByActivityThread()
                ?: getCurrentProcessNameByActivityManager(context)
        }
    }
}