package com.huya.mobile.uinspector.plugins

/**
 * @author YvesCheung
 * 2021/2/1
 */
interface UInspectorPlugins {

    operator fun <Plugin : UInspectorPlugin> get(cls: Class<out Plugin>): List<Plugin>

    /**
     * Insert a high priority [plugin] into the head of queue.
     * If a plugin with the same [UInspectorPlugin.uniqueKey] already exists, it will be replaced by [plugin]
     */
    fun <Plugin : UInspectorPlugin> prepend(cls: Class<out Plugin>, plugin: Plugin)

    /**
     * Add a low priority [plugin] to the tail of queue.
     * If a plugin with the same [UInspectorPlugin.uniqueKey] already exists, this call will be ignored.
     */
    fun <Plugin : UInspectorPlugin> append(cls: Class<out Plugin>, plugin: Plugin)
}