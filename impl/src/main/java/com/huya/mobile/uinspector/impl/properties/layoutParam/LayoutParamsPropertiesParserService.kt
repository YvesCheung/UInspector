package com.huya.mobile.uinspector.impl.properties.layoutParam

import android.view.View
import android.view.ViewGroup

/**
 * Using Java SPI: To add your custom layoutParam's properties
 *
 * @author YvesCheung
 * 2021/1/3
 */
interface LayoutParamsPropertiesParserService {

    fun tryCreate(view: View, lp: ViewGroup.LayoutParams): LayoutParamsPropertiesParser<out ViewGroup.LayoutParams>?
}