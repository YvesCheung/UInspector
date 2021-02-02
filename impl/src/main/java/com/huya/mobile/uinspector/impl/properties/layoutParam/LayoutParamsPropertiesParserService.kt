package com.huya.mobile.uinspector.impl.properties.layoutParam

import android.view.View
import android.view.ViewGroup
import com.huya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * To add your custom layoutParam's properties
 *
 * @author YvesCheung
 * 2021/1/3
 */
interface LayoutParamsPropertiesParserService : UInspectorPlugin {

    fun tryCreate(view: View, lp: ViewGroup.LayoutParams):
        LayoutParamsPropertiesParser<out ViewGroup.LayoutParams>?
}