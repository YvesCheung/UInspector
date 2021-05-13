package com.pitaya.mobile.uinspector.optional.multitype

import android.content.Context
import com.google.auto.service.AutoService
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfoPlugin
import com.pitaya.mobile.uinspector.plugins.UInspectorPluginService
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugins

/**
 * @author YvesCheung
 * 2021/3/31
 */
@AutoService(UInspectorPluginService::class)
class UInspectorMultiTypeService : UInspectorPluginService {

    override fun onCreate(context: Context, plugins: UInspectorPlugins) {
        plugins.append(HierarchyExtraInfoPlugin::class.java, MultiTypeHierarchyPlugin(context))
    }

    companion object {
        const val PluginKey = "MultiType"
    }
}