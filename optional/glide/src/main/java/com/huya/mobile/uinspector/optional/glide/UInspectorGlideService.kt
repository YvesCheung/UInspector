package com.huya.mobile.uinspector.optional.glide

import android.content.Context
import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserPlugin
import com.huya.mobile.uinspector.plugins.UInspectorPluginService
import com.huya.mobile.uinspector.plugins.UInspectorPlugins

/**
 * @author YvesCheung
 * 2021/1/7
 */
class UInspectorGlideService : UInspectorPluginService {

    override fun onCreate(context: Context, plugins: UInspectorPlugins) {
        plugins.prepend(ViewPropertiesParserPlugin::class.java, GlidePropertiesParserPlugin())
    }
}