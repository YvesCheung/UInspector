package com.huya.mobile.uinspector.optional.glide

import android.content.Context
import com.huya.mobile.uinspector.properties.view.ViewPropertiesPlugin
import com.huya.mobile.uinspector.plugins.UInspectorPluginService
import com.huya.mobile.uinspector.plugins.UInspectorPlugins

/**
 * @author YvesCheung
 * 2021/1/7
 */
class UInspectorGlideService : UInspectorPluginService {

    override fun onCreate(context: Context, plugins: UInspectorPlugins) {
        plugins.append(ViewPropertiesPlugin::class.java, GlidePropertiesParserPlugin())
    }
}