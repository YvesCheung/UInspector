package com.huya.mobile.uinspector.impl.properties.layoutParam

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.huya.mobile.uinspector.util.dpStr
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/4
 */
open class LayoutParamsPropertiesParser<LP : ViewGroup.LayoutParams>(protected val lp: LP) {

    open fun parse(@Output props: MutableMap<String, Any?>) {
        props["layout_width"] = layoutToString(lp.width)
        props["layout_height"] = layoutToString(lp.height)
    }

    private fun layoutToString(l: Int): String? {
        return when (l) {
            MATCH_PARENT -> "MATCH_PARENT"
            WRAP_CONTENT -> "WRAP_CONTENT"
            else -> l.dpStr
        }
    }
}