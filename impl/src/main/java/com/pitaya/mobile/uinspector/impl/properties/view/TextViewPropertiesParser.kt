package com.pitaya.mobile.uinspector.impl.properties.view

import android.os.Build
import android.view.Gravity
import android.widget.TextView
import com.pitaya.mobile.uinspector.util.colorToString
import com.pitaya.mobile.uinspector.util.gravityToString
import com.pitaya.mobile.uinspector.util.quote
import com.pitaya.mobile.uinspector.util.spStr
import com.github.yvescheung.whisper.Output

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (view.isSingleLine) {
                props["isSingleLine"] = view.isSingleLine
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            if (view.maxLines != Int.MAX_VALUE && view.maxLines != -1) {
                props["maxLines"] = view.maxLines
            }

            if (view.maxHeight != Int.MAX_VALUE && view.maxHeight != -1) {
                props["maxHeight"] = view.maxHeight
            }

            if (view.minHeight != 0 && view.minHeight != -1) {
                props["minHeight"] = view.minHeight
            }

            if (view.maxWidth != Int.MAX_VALUE && view.maxWidth != -1) {
                props["maxWidth"] = view.maxWidth
            }

            if (view.minWidth != 0 && view.minWidth != -1) {
                props["minWidth"] = view.minWidth
            }

            if (view.maxEms == view.minEms && view.maxEms != -1) {
                props["ems"] = view.maxEms
            } else {
                if (view.maxEms != Int.MAX_VALUE && view.maxEms != -1) {
                    props["maxEms"] = view.maxEms
                }

                if (view.minEms != 0 && view.minEms != -1) {
                    props["minEms"] = view.minEms
                }
            }
        }
    }
}