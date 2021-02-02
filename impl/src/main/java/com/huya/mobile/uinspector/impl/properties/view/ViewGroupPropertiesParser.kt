package com.huya.mobile.uinspector.impl.properties.view

import android.view.ViewGroup
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/27
 */
open class ViewGroupPropertiesParser<VG : ViewGroup>(view: VG) : ViewPropertiesParser<VG>(view) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        if (view.clipChildren) {
            props["clipChildren"] = view.clipChildren
        }
        if (view.clipToPadding) {
            props["clipToPadding"] = view.clipToPadding
        }

        props["layoutMode"] = if (view.layoutMode == ViewGroup.LAYOUT_MODE_CLIP_BOUNDS) {
            "clipBounds"
        } else {
            "opticalBounds"
        }

        props["descendantFocusability"] = when (view.descendantFocusability) {
            ViewGroup.FOCUS_BEFORE_DESCENDANTS -> "beforeDescendants"
            ViewGroup.FOCUS_AFTER_DESCENDANTS -> "afterDescendants"
            else -> "blocksDescendants"
        }
    }
}