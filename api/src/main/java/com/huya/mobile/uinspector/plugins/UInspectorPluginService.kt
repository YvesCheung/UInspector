package com.huya.mobile.uinspector.plugins

import android.content.Context

/**
 * @author YvesCheung
 * 2021/2/1
 */
interface UInspectorPluginService {

    fun onCreate(context: Context, plugins: UInspectorPlugins)
}