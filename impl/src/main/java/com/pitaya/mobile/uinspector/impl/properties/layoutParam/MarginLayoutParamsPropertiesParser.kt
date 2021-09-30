package com.pitaya.mobile.uinspector.impl.properties.layoutParam

import android.os.Build
import android.view.View.LAYOUT_DIRECTION_RTL
import android.view.ViewGroup

/**
 * @author YvesCheung
 * 2021/9/30
 */
open class MarginLayoutParamsPropertiesParser<P : ViewGroup.MarginLayoutParams>(lp: P) :
    LayoutParamsPropertiesParser<P>(lp) {

    override fun parse(props: MutableMap<String, Any?>) {
        super.parse(props)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (lp.layoutDirection == LAYOUT_DIRECTION_RTL) {
                props["isLayoutRtl"] = "true"
            }
        }
    }
}