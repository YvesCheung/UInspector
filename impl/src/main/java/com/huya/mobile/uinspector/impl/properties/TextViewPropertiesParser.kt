package com.huya.mobile.uinspector.impl.properties

import android.os.Build
import android.widget.TextView

/**
 * @author YvesCheung
 * 2020/12/31
 */
class TextViewPropertiesParser(view: TextView) : ViewPropertiesParser<TextView>(view) {

    override fun parse(props: MutableMap<String, Any?>) {
        super.parse(props)
        props["text"] = view.text
        props["textSize"] = view.textSize
        props["hint"] = view.hint
        props["ellipsize"] = view.ellipsize
        props["gravity"] = view.gravity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            props["maxLines"] = view.maxLines
        }
    }
}