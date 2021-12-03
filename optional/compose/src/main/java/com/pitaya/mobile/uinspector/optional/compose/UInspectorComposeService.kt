package com.pitaya.mobile.uinspector.optional.compose

import android.content.Context
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import com.google.auto.service.AutoService
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfoPlugin
import com.pitaya.mobile.uinspector.hierarchy.HitTestFactoryPlugin
import com.pitaya.mobile.uinspector.hierarchy.LayerFactoryPlugin
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeLayerFactoryPlugin
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.extra.ComposeHierarchyExtraInfoPlugin
import com.pitaya.mobile.uinspector.optional.compose.properties.ComposePropertiesParserFactory
import com.pitaya.mobile.uinspector.optional.compose.properties.DefaultComposeModifiersParserFactory
import com.pitaya.mobile.uinspector.optional.compose.panel.UInspectorComposeChildPanelPlugin
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
        isDebugInspectorInfoEnabled = true
        plugins.prepend(UInspectorChildPanelPlugin::class.java, UInspectorComposeChildPanelPlugin())
        plugins.append(LayerFactoryPlugin::class.java, ComposeLayerFactoryPlugin())
        plugins.append(HitTestFactoryPlugin::class.java, ComposeHitTestFactoryPlugin())
        plugins.append(ComposePropertiesParserFactory::class.java, DefaultComposeModifiersParserFactory())
        plugins.append(HierarchyExtraInfoPlugin::class.java, ComposeHierarchyExtraInfoPlugin())
    }

    companion object {

        const val PluginKey = "JetpackCompose"
    }
}