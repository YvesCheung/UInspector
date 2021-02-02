package com.huya.mobile.uinspector.impl

import android.content.Context
import com.huya.mobile.uinspector.plugins.UInspectorPluginService
import com.huya.mobile.uinspector.plugins.UInspectorPlugins
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelPlugin

/**
 * @author YvesCheung
 * 2021/2/1
 */
class UInspectorDefaultPluginService : UInspectorPluginService {

    override fun onCreate(context: Context, plugins: UInspectorPlugins) {
        plugins.prepend(UInspectorChildPanelPlugin::class.java, UInspectorDefaultChildPanelPlugin())
    }
}