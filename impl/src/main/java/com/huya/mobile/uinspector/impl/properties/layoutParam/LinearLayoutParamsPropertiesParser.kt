package com.huya.mobile.uinspector.impl.properties.layoutParam

import android.widget.LinearLayout
import com.huya.mobile.uinspector.impl.utils.gravityToString

/**
 * @author YvesCheung
 * 2021/1/4
 */
open class LinearLayoutParamsPropertiesParser(lp: LinearLayout.LayoutParams) :
    LayoutParamsPropertiesParser<LinearLayout.LayoutParams>(lp) {

    override fun parse(props: MutableMap<String, Any?>) {
        super.parse(props)

        if (lp.weight != 0f) {
            props["layout_weight"] = lp.weight
        }

        if (lp.gravity != -1) {
            props["layout_gravity"] = gravityToString(lp.gravity)
        }
    }
}