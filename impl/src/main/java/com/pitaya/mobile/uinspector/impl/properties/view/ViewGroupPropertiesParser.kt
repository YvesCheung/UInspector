package com.pitaya.mobile.uinspector.impl.properties.view

import android.os.Build
import android.view.ViewGroup
import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/27
 */
open class ViewGroupPropertiesParser<VG : ViewGroup>(view: VG) : ViewPropertiesParser<VG>(view) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (!view.clipChildren) { //default true
                props["clipChildren"] = view.clipChildren
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!view.clipToPadding) { //default true
                props["clipToPadding"] = view.clipToPadding
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            props["layoutMode"] =
                if (view.layoutMode == ViewGroup.LAYOUT_MODE_CLIP_BOUNDS) {
                    "clipBounds"
                } else {
                    "opticalBounds"
                }
        }

        if (view.descendantFocusability != ViewGroup.FOCUS_BEFORE_DESCENDANTS) {
            props["descendantFocusability"] =
                when (view.descendantFocusability) {
                    ViewGroup.FOCUS_BEFORE_DESCENDANTS -> "beforeDescendants"
                    ViewGroup.FOCUS_AFTER_DESCENDANTS -> "afterDescendants"
                    else -> "blocksDescendants"
                }
        }
    }
}