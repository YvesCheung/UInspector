package com.huya.mobile.uinspector.impl.properties

import android.widget.FrameLayout

/**
 * @author YvesCheung
 * 2021/1/4
 */
class FrameLayoutPropertiesParser(view: FrameLayout) : ViewPropertiesParser<FrameLayout>(view) {

    override fun parse(props: MutableMap<String, Any?>) {
        super.parse(props)

        if (view.measureAllChildren) {
            props["measureAllChildren"] = view.measureAllChildren
        }
    }
}