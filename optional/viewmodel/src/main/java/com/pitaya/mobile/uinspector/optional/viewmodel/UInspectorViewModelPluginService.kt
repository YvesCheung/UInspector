package com.pitaya.mobile.uinspector.optional.viewmodel

import android.content.Context
import com.google.auto.service.AutoService
import com.pitaya.mobile.uinspector.plugins.UInspectorPluginService
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugins
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelPlugin

/**
 * @author YvesCheung
 * 2021/2/1
 */
@AutoService(UInspectorPluginService::class)
class UInspectorViewModelPluginService : UInspectorPluginService {

    override fun onCreate(context: Context, plugins: UInspectorPlugins) {
        plugins.append(
            UInspectorChildPanelPlugin::class.java,
            UInspectorViewModelPanelPlugin(PLUGIN_KEY)
        )
    }

    companion object {

        /**
         * High priority or custom plugins can override the default plugin with the same key
         */
        const val PLUGIN_KEY = "ViewModel"
    }
}