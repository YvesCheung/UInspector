package com.pitaya.mobile.uinspector.hierarchy

import android.app.Activity
import android.text.SpannableStringBuilder
import android.view.View
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * Add custom extra info into [SpannableStringBuilder]
 *
 * @author YvesCheung
 * 2021/1/6
 */
interface HierarchyExtraInfoPlugin : UInspectorPlugin {

    fun create(activity: Activity, targetView: View): Set<HierarchyExtraInfo>

    fun create(activity: Activity, targetLayer: Layer): Set<HierarchyExtraInfo> {
        if (targetLayer is AndroidView) {
            return create(activity, targetLayer.view)
        }
        return emptySet()
    }
}