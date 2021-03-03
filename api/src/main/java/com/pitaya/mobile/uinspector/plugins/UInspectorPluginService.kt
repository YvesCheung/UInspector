package com.pitaya.mobile.uinspector.plugins

import android.content.Context
import androidx.annotation.AnyThread
import com.pitaya.mobile.uinspector.UInspector

/**
 * Using Java SPI mechanism, you can create your own [UInspectorPluginService] which
 * has the registry instance [UInspectorPlugins]
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