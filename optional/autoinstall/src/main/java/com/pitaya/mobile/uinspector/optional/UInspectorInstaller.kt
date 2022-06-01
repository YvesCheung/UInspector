package com.pitaya.mobile.uinspector.optional

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Process.myPid
import androidx.annotation.RestrictTo
import com.pitaya.mobile.uinspector.UInspector


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
class UInspectorInstaller : ContentProvider() {

    override fun onCreate(): Boolean {
        val ctx = requireNotNull(context)
        val processName = getCurrentProcessName(ctx)
        if (processName == null /*unknown?*/ || processName == ctx.packageName /*main process*/) {
            UInspector.create(ctx)
        }
        return true
    }

    override fun query(uri: Uri, strings: Array<String?>?, s: String?, strings1: Array<String?>?, s1: String?): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String?>?): Int {
        return 0
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String?>?): Int {
        return 0
    }

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