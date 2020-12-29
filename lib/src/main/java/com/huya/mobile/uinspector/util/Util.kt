package com.huya.mobile.uinspector.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log

/**
 * @author YvesCheung
 * 2020/12/29
 */
internal fun tryGetActivity(context: Context?): Activity? {
    if (context is Activity) {
        return context
    } else if (context is ContextWrapper) {
        return tryGetActivity(context.baseContext)
    }
    return null
}

internal fun log(value: String) {
    Log.d(LibName, value)
}

internal const val LibName = "UInspector"