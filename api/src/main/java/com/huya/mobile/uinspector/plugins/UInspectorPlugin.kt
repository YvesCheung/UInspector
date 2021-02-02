package com.huya.mobile.uinspector.plugins

/**
 * Marker interface
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