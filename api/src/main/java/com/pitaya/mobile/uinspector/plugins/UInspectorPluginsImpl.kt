package com.pitaya.mobile.uinspector.plugins

import androidx.annotation.GuardedBy

/**
 * @author YvesCheung
 * 2021/2/1
 */
internal class UInspectorPluginsImpl(
    private val onFirstLoad: (plugins: UInspectorPlugins) -> Unit
) : UInspectorPlugins {

    @GuardedBy("plugins")
    @Volatile
    private var loaded = false

    private val plugins = mutableMapOf<Class<out UInspectorPlugin>, MutableList<UInspectorPlugin>>()

    override operator fun <Plugin : UInspectorPlugin> get(cls: Class<Plugin>): List<Plugin> {
        return synchronized(plugins) {
            if (!loaded) {
                onFirstLoad(this)
                loaded = true
            }
            @Suppress("UNCHECKED_CAST")
            (plugins[cls] as? List<Plugin>) ?: emptyList()
        }
    }

    override fun <Plugin : UInspectorPlugin> prepend(cls: Class<Plugin>, plugin: Plugin) {
        synchronized(plugins) {
            val pluginList = plugins.getOrPut(cls) { mutableListOf() }
            pluginList.removeAll { it.uniqueKey == plugin.uniqueKey }
            pluginList.add(0, plugin)
        }
    }

    override fun <Plugin : UInspectorPlugin> append(cls: Class<Plugin>, plugin: Plugin) {
        synchronized(plugins) {
            val pluginList = plugins.getOrPut(cls) { mutableListOf() }
            if (pluginList.all { it.uniqueKey != plugin.uniqueKey }) {
                pluginList.add(plugin)
            }
        }
    }
}