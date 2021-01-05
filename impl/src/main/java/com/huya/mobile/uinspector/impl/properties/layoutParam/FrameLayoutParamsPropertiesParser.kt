package com.huya.mobile.uinspector.impl.properties.layoutParam

import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY
import com.huya.mobile.uinspector.util.gravityToString

/**
 * @author YvesCheung
 * 2021/1/4
 */
open class FrameLayoutParamsPropertiesParser(lp: FrameLayout.LayoutParams) :
    LayoutParamsPropertiesParser<FrameLayout.LayoutParams>(lp) {

    override fun parse(props: MutableMap<String, Any?>) {
        super.parse(props)

        if (lp.gravity != UNSPECIFIED_GRAVITY) {
            props["layout_gravity"] = gravityToString(lp.gravity)
        }
    }
}