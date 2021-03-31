package com.pitaya.mobile.uinspector.plugins

import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfoPlugin
import com.pitaya.mobile.uinspector.hierarchy.HitTestFactoryPlugin
import com.pitaya.mobile.uinspector.hierarchy.LayerFactoryPlugin
import com.pitaya.mobile.uinspector.properties.layoutParam.LayoutParamsPropertiesPlugin
import com.pitaya.mobile.uinspector.properties.view.ViewPropertiesPlugin

/**
 * Plugins that provide extra features for [UInspector] can be registered
 * to [UInspectorPlugins] through [UInspectorPluginService].
 *
 * @see HitTestFactoryPlugin
 * @see LayerFactoryPlugin
 * @see LayoutParamsPropertiesPlugin
 * @see ViewPropertiesPlugin
 * @see HierarchyExtraInfoPlugin
 *
 * @author YvesCheung
 * 2021/2/1
 */
interface UInspectorPlugin {

    /**
     * The unique key represent for this plugin.
     * Use in [UInspectorPlugins.prepend] and [UInspectorPlugins.append].
     */
    val uniqueKey: String
}