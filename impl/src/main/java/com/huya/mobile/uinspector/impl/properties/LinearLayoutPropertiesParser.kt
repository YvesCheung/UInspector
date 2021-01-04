package com.huya.mobile.uinspector.impl.properties

import android.os.Build
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import com.huya.mobile.uinspector.impl.utils.gravityToString

/**
 * @author YvesCheung
 * 2021/1/4
 */
class LinearLayoutPropertiesParser(view: LinearLayout) : ViewPropertiesParser<LinearLayout>(view) {

    override fun parse(props: MutableMap<String, Any?>) {
        super.parse(props)

        props["orientation"] =
            if (view.orientation == HORIZONTAL) "HORIZONTAL" else "VERTICAL"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (view.gravity != Gravity.START or Gravity.TOP) {
                props["gravity"] = gravityToString(view.gravity)
            }
        }

        if (view.weightSum > 0f) {
            props["weightSum"] = view.weightSum
        }

        if (view.isBaselineAligned) {
            props["isBaselineAligned"] = view.isBaselineAligned
        }
    }
}