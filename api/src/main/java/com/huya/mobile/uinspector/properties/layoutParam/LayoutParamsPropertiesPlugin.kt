package com.huya.mobile.uinspector.properties.layoutParam

import android.view.View
import android.view.ViewGroup
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.plugins.UInspectorPlugin
import com.huya.mobile.uinspector.properties.PropertiesParser

/**
 * To add your custom layoutParam's properties
 *
 * @author YvesCheung
 * 2021/1/3
 */
interface LayoutParamsPropertiesPlugin : UInspectorPlugin {

    fun tryCreate(view: View, lp: ViewGroup.LayoutParams): PropertiesParser?

    companion object {

        private val parserFactory by lazy { UInspector.plugins[LayoutParamsPropertiesPlugin::class.java] }

        fun of(view: View, lp: ViewGroup.LayoutParams): PropertiesParser? {
            for (service in parserFactory) {
                val parser = service.tryCreate(view, lp)
                if (parser != null) {
                    return parser
                }
            }
            return null
        }
    }
}