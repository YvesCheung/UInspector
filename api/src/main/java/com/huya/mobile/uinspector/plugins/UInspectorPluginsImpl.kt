package com.huya.mobile.uinspector.plugins

/**
 * @author YvesCheung
 * 2021/2/1
 */
internal class UInspectorPluginsImpl : UInspectorPlugins {

    private val plugins = mutableMapOf<Class<out UInspectorPlugin>, MutableList<UInspectorPlugin>>()

    override operator fun <Plugin : UInspectorPlugin> get(cls: Class<out Plugin>): List<Plugin> {
        return synchronized(plugins) {
            @Suppress("UNCHECKED_CAST")
            (plugins[cls] as? List<Plugin>) ?: emptyList()
        }
    }

    override fun <Plugin : UInspectorPlugin> prepend(cls: Class<out Plugin>, plugin: Plugin) {
        synchronized(plugins) {
            val pluginList = plugins.getOrPut(cls) { mutableListOf() }
            pluginList.removeIf { it.uniqueKey == plugin.uniqueKey }
            pluginList.add(0, plugin)
        }
    }

    override fun <Plugin : UInspectorPlugin> append(cls: Class<out Plugin>, plugin: Plugin) {
        synchronized(plugins) {
            val pluginList = plugins.getOrPut(cls) { mutableListOf() }
            if (pluginList.all { it.uniqueKey != plugin.uniqueKey }) {
                pluginList.add(plugin)
            }
        }
    }
}