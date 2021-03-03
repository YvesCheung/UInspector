package com.pitaya.mobile.uinspector.impl.properties.view

import android.os.Build
import android.view.Gravity
import android.widget.TextView
import com.pitaya.mobile.uinspector.util.colorToString
import com.pitaya.mobile.uinspector.util.gravityToString
import com.pitaya.mobile.uinspector.util.quote
import com.pitaya.mobile.uinspector.util.spStr
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2020/12/31
 */
open class TextViewPropertiesParser(view: TextView) : ViewPropertiesParser<TextView>(view) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)
        if (view.text != null) {
            props["text"] = view.text.quote()
        }

        props["textSize"] = view.textSize.spStr

        props["textColor"] = colorToString(view.textColors)

        if (view.typeface != null) {
            if (view.typeface.isBold) {
                props["isBold"] = "true"
            }

            if (view.typeface.isItalic) {
                props["isItalic"] = "true"
            }
        }

        if (view.hint != null) {
            props["hint"] = view.hint.quote()
        }

        if (view.ellipsize != null) {
            props["ellipsize"] = view.ellipsize
        }

        if (view.gravity != Gravity.TOP or Gravity.START) {
            props["gravity"] = gravityToString(view.gravity)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            if (view.maxLines != Int.MAX_VALUE) {
                props["maxLines"] = view.maxLines
            }
        }
    }
}