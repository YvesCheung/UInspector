package com.pitaya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.text.SpannableStringBuilder
import android.view.View
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.util.newLine
import com.pitaya.mobile.uinspector.util.withColor

/**
 * @author YvesCheung
 * 2021/1/6
 */
open class HierarchyActivityInfo(private val activity: Activity) : HierarchyExtraInfo {

    override fun beforeHierarchy(index: Int, view: View, s: SpannableStringBuilder) {
        if (index == 0) {
            s.withColor(activity) {
                newLine(0) {
                    append(activity::class.java.canonicalName ?: activity::class.java.name)
                }
            }
        }
    }
}