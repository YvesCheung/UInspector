package com.pitaya.mobile.uinspector.plugins

import android.content.Context
import androidx.annotation.AnyThread
import com.pitaya.mobile.uinspector.UInspector

/**
 * Using Java SPI mechanism, you can create your own [UInspectorPluginService] which
 * has the registry instance [UInspectorPlugins].
 *
 * The constructor of the service is called when [UInspector] is initialized,
 * while [onCreate] is called when [UInspector] is used for the first time.
 *
 * @author YvesCheung
 * 2021/2/1
 */
interface UInspectorPluginService {

    /**
     * Once called when the [UInspector] is first started.
     */
    @AnyThread
    fun onCreate(context: Context, plugins: UInspectorPlugins)
}