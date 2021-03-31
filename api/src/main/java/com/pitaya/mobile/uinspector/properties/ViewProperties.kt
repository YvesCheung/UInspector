package com.pitaya.mobile.uinspector.properties

import android.view.View
import com.pitaya.mobile.uinspector.properties.layoutParam.LayoutParamsPropertiesPlugin
import com.pitaya.mobile.uinspector.properties.view.ViewPropertiesPlugin

/**
 * @author YvesCheung
 * 2020/12/31
 */
class ViewProperties(
    view: View,
    private val actual: LinkedHashMap<String, Any?> = LinkedHashMap()
) : Map<String, Any?> by actual {


    init {
        ViewPropertiesPlugin.of(view)?.parse(actual)
        val lp = view.layoutParams
        if (lp != null) {
            LayoutParamsPropertiesPlugin.of(view, lp)?.parse(actual)
        }
    }
}