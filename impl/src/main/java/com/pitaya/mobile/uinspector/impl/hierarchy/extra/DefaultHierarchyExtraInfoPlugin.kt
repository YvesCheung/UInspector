package com.pitaya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.os.Build
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfoPlugin
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.impl.UInspectorDefaultPluginService.Companion.PLUGIN_KEY

/**
 * @author YvesCheung
 * 2021/1/6
 */
class DefaultHierarchyExtraInfoPlugin : HierarchyExtraInfoPlugin {

    override fun create(activity: Activity, targetLayer: Layer): Set<HierarchyExtraInfo> {
        val extraInfo = mutableSetOf(
            HierarchyActivityInfo(activity),
            HierarchyFragmentInfo(activity),
            RecyclerViewExtraInfo(activity)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            extraInfo += TargetViewSourceLayoutIdInfo(activity)
        }
        return extraInfo
    }

    override val uniqueKey: String = PLUGIN_KEY
}