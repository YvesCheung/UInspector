package com.pitaya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.text.SpannableStringBuilder
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.util.canonicalName
import com.pitaya.mobile.uinspector.util.newLine
import com.pitaya.mobile.uinspector.util.withColor

/**
 * @author YvesCheung
 * 2021/1/6
 */
open class HierarchyActivityInfo(private val activity: Activity) : HierarchyExtraInfo {

    override fun beforeHierarchy(index: Int, layer: Layer, s: SpannableStringBuilder) {
        if (index == 0) {
            s.withColor(activity) {
                newLine(0) {
                    append(activity.canonicalName)
                }
            }
        }
    }
}