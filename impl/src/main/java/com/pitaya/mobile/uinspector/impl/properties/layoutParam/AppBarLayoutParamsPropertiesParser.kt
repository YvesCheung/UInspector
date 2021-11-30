package com.pitaya.mobile.uinspector.impl.properties.layoutParam

import android.view.View
import com.google.android.material.appbar.AppBarLayout.LayoutParams
import com.google.android.material.appbar.AppBarLayout.LayoutParams.*
import com.pitaya.mobile.uinspector.util.canonicalName
import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/9/30
 */
open class AppBarLayoutParamsPropertiesParser<P : LayoutParams>(val view: View, lp: P) :
    LinearLayoutParamsPropertiesParser<P>(lp) {

    private val scrollFlags = listOf(
        SCROLL_FLAG_NO_SCROLL,
        SCROLL_FLAG_SCROLL,
        SCROLL_FLAG_EXIT_UNTIL_COLLAPSED,
        SCROLL_FLAG_ENTER_ALWAYS,
        SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED,
        SCROLL_FLAG_SNAP,
        SCROLL_FLAG_SNAP_MARGINS
    )

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        props["scrollFlags"] = scrollFlags
            .filter { flag -> lp.scrollFlags and flag != 0 }
            .joinToString(separator = "|") { flag -> scrollFlagToString(flag) }

        val interpolator = lp.scrollInterpolator
        if (interpolator != null) {
            props["scrollInterpolator"] = interpolator.canonicalName
        }
    }

    private fun scrollFlagToString(@ScrollFlags flag: Int): String {
        return when (flag) {
            SCROLL_FLAG_NO_SCROLL -> "noScroll"
            SCROLL_FLAG_SCROLL -> "scroll"
            SCROLL_FLAG_EXIT_UNTIL_COLLAPSED -> "exitUntilCollapsed"
            SCROLL_FLAG_ENTER_ALWAYS -> "enterAlways"
            SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED -> "enterAlwaysCollapsed"
            SCROLL_FLAG_SNAP -> "snap"
            SCROLL_FLAG_SNAP_MARGINS -> "snapMargins"
            else -> "unknown($flag)"
        }
    }
}