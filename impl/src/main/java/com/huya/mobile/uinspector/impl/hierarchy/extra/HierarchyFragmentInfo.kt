package com.huya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.text.SpannableStringBuilder
import android.view.View
import com.huya.mobile.uinspector.hierarchy.FragmentsFinder
import com.huya.mobile.uinspector.util.canonicalName
import com.huya.mobile.uinspector.util.newLine
import com.huya.mobile.uinspector.util.withColor

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