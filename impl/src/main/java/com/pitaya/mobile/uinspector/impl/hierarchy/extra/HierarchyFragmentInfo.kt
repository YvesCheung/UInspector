package com.pitaya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.text.SpannableStringBuilder
import android.view.View
import com.pitaya.mobile.uinspector.hierarchy.FragmentsFinder
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.util.canonicalName
import com.pitaya.mobile.uinspector.util.newLine
import com.pitaya.mobile.uinspector.util.withColor

/**
 * @author YvesCheung
 * 2021/1/6
 */
@Suppress("MemberVisibilityCanBePrivate")
open class HierarchyFragmentInfo(val activity: Activity) : HierarchyExtraInfo {

    protected val records = FragmentsFinder.findFragments(activity)

    override fun beforeHierarchy(index: Int, view: View, s: SpannableStringBuilder) {
        val fragment = records[view]
        if (fragment != null) {
            s.withColor(activity) {
                newLine(index) {
                    append(fragment.canonicalName)
                }
            }
        }
    }
}