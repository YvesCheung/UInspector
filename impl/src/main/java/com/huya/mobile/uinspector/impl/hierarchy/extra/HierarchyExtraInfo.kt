package com.huya.mobile.uinspector.impl.hierarchy.extra

import android.text.SpannableStringBuilder
import android.view.View
import com.yy.mobile.whisper.Output

/**
 * @see HierarchyExtraInfoService
 *
 * @author YvesCheung
 * 2021/1/6
 */
interface HierarchyExtraInfo {

    fun beforeHierarchy(index: Int, view: View, @Output s: SpannableStringBuilder) {}

    fun afterHierarchy(index: Int, view: View, @Output s: SpannableStringBuilder) {}
}