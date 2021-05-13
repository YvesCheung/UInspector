package com.pitaya.mobile.uinspector.optional.fresco

import android.content.Context
import com.google.auto.service.AutoService
import com.pitaya.mobile.uinspector.properties.view.ViewPropertiesPlugin
import com.pitaya.mobile.uinspector.plugins.UInspectorPluginService
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugins

/**
 * @author YvesCheung
 * 2021/2/1
 */
@AutoService(UInspectorPluginService::class)
open class UInspectorFrescoService : UInspectorPluginService {

    override fun onCreate(context: Context, plugins: UInspectorPlugins) {
        plugins.append(ViewPropertiesPlugin::class.java, FrescoPropertiesParserPlugin())
    }
}