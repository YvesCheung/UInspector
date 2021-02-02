package com.huya.mobile.uinspector.impl.properties.view

import android.view.View
import com.huya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * To add your custom view's properties
 *
 * @author YvesCheung
 * 2021/1/3
 */
interface ViewPropertiesParserPlugin : UInspectorPlugin {

    fun tryCreate(v: View): ViewPropertiesParser<out View>?
}