package com.pitaya.mobile.uinspector.optional.compose

import android.content.Context
import com.google.auto.service.AutoService
import com.pitaya.mobile.uinspector.hierarchy.HitTestFactoryPlugin
import com.pitaya.mobile.uinspector.hierarchy.LayerFactoryPlugin
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeLayerFactoryPlugin
import com.pitaya.mobile.uinspector.optional.compose.properties.ComposePropertiesParserFactory
import com.pitaya.mobile.uinspector.optional.compose.properties.DefaultComposePropertiesParserFactory
import com.pitaya.mobile.uinspector.optional.compose.properties.UInspectorComposeChildPanelPlugin
import com.pitaya.mobile.uinspector.optional.compose.touch.ComposeHitTestFactoryPlugin
import com.pitaya.mobile.uinspector.plugins.UInspectorPluginService
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugins
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelPlugin

/**
 * @author YvesCheung
 * 2021/2/1
 */
@AutoService(UInspectorPluginService::class)
class UInspectorComposeService : UInspectorPluginService {

    override fun onCreate(context: Context, plugins: UInspectorPlugins) {
        plugins.prepend(UInspectorChildPanelPlugin::class.java, UInspectorComposeChildPanelPlugin())
        plugins.prepend(LayerFactoryPlugin::class.java, ComposeLayerFactoryPlugin())
        plugins.prepend(HitTestFactoryPlugin::class.java, ComposeHitTestFactoryPlugin())
        plugins.append(ComposePropertiesParserFactory::class.java, DefaultComposePropertiesParserFactory())
    }

    companion object {

        const val PluginKey = "JetpackCompose"
    }
}