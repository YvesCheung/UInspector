package com.pitaya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.view.View
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfoPlugin
import com.pitaya.mobile.uinspector.impl.UInspectorDefaultPluginService.Companion.PLUGIN_KEY

/**
 * @author YvesCheung
 * 2021/1/6
 */
class DefaultHierarchyExtraInfoPlugin : HierarchyExtraInfoPlugin {

    override fun create(activity: Activity, targetView: View): Set<HierarchyExtraInfo> = setOf(
        HierarchyActivityInfo(activity),
        HierarchyFragmentInfo(activity),
        RecyclerViewExtraInfo(activity)
    )

    override val uniqueKey: String = PLUGIN_KEY
}