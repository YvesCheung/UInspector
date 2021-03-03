package com.pitaya.mobile.uinspector.impl

import android.content.Context
import com.pitaya.mobile.uinspector.impl.properties.layoutParam.UInspectorDefaultLayoutParamsPropertiesPlugin
import com.pitaya.mobile.uinspector.impl.properties.view.UInspectorDefaultViewPropertiesPlugin
import com.pitaya.mobile.uinspector.plugins.UInspectorPluginService
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugins
import com.pitaya.mobile.uinspector.properties.layoutParam.LayoutParamsPropertiesPlugin
import com.pitaya.mobile.uinspector.properties.view.ViewPropertiesPlugin
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelPlugin

/**
 * @author YvesCheung
 * 2021/2/1
 */
class UInspectorDefaultPluginService : UInspectorPluginService {

    override fun onCreate(context: Context, plugins: UInspectorPlugins) {
        plugins.append(UInspectorChildPanelPlugin::class.java, UInspectorDefaultChildPanelPlugin())
        plugins.append(ViewPropertiesPlugin::class.java, UInspectorDefaultViewPropertiesPlugin())
        plugins.append(LayoutParamsPropertiesPlugin::class.java, UInspectorDefaultLayoutParamsPropertiesPlugin())
    }

    companion object {

        /**
         * High priority or custom plugins can override the default plugin with the same key
         */
        const val PLUGIN_KEY = "Default"
    }
}