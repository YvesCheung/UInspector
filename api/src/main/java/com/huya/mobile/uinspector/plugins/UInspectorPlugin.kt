package com.huya.mobile.uinspector.plugins

import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.hierarchy.HitTestFactoryPlugin
import com.huya.mobile.uinspector.hierarchy.LayerFactoryPlugin
import com.huya.mobile.uinspector.properties.layoutParam.LayoutParamsPropertiesPlugin
import com.huya.mobile.uinspector.properties.view.ViewPropertiesPlugin

/**
 * Plugins that provide extra features for [UInspector] can be registered
 * to [UInspectorPlugins] through [UInspectorPluginService].
 *
 * @see HitTestFactoryPlugin
 * @see LayerFactoryPlugin
 * @see LayoutParamsPropertiesPlugin
 * @see ViewPropertiesPlugin
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