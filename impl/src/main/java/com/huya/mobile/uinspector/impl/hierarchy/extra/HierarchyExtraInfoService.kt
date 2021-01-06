package com.huya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.text.SpannableStringBuilder
import android.view.View

/**
 * Java SPI: add custom extra info into [SpannableStringBuilder]
 *
 * @author YvesCheung
 * 2021/1/6
 */
interface HierarchyExtraInfoService {

    fun create(activity: Activity, targetView: View): Set<HierarchyExtraInfo>
}