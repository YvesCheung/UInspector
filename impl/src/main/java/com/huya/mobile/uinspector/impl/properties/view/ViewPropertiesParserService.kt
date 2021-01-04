package com.huya.mobile.uinspector.impl.properties.view

import android.view.View

/**
 * Using Java SPI: To add your custom view's properties
 *
 * @author YvesCheung
 * 2021/1/3
 */
interface ViewPropertiesParserService {

    fun tryCreate(v: View): ViewPropertiesParser<out View>?
}