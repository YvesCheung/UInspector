package com.huya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.text.SpannableStringBuilder
import android.view.View
import com.huya.mobile.uinspector.util.newLine
import com.huya.mobile.uinspector.util.withColor

/**
 * @author YvesCheung
 * 2021/1/6
 */
open class HierarchyActivityInfo(private val activity: Activity) : HierarchyExtraInfo {

    override fun beforeHierarchy(index: Int, view: View, s: SpannableStringBuilder) {
        if (index == 0) {
            s.withColor(activity) {
                newLine(0) {
                    append(activity::class.java.canonicalName)
                }
            }
        }
    }
}