package com.huya.mobile.uinspector.properties.view

import android.view.View
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.plugins.UInspectorPlugin
import com.huya.mobile.uinspector.properties.PropertiesParser

/**
 * To add your custom view's properties
 *
 * @author YvesCheung
 * 2021/1/3
 */
interface ViewPropertiesPlugin : UInspectorPlugin {

    fun tryCreate(view: View): PropertiesParser?

    companion object {

        private val parserFactory by lazy {
            UInspector.plugins[ViewPropertiesPlugin::class.java]
        }

        fun of(view: View): PropertiesParser? {
            for (service in parserFactory) {
                val parser = service.tryCreate(view)
                if (parser != null) {
                    return parser
                }
            }
            return null
        }
    }
}