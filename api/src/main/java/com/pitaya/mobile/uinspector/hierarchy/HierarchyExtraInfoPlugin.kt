package com.pitaya.mobile.uinspector.hierarchy

import android.app.Activity
import android.text.SpannableStringBuilder
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * Add custom extra info into [SpannableStringBuilder]
 *
 * @author YvesCheung
 * 2021/1/6
 */
interface HierarchyExtraInfoPlugin : UInspectorPlugin {

    fun create(activity: Activity, targetLayer: Layer): Set<HierarchyExtraInfo>
}