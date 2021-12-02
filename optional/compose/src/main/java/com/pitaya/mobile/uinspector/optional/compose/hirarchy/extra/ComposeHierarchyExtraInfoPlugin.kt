package com.pitaya.mobile.uinspector.optional.compose.hirarchy.extra

import android.app.Activity
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfoPlugin
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.UInspectorComposeService.Companion.PluginKey

/**
 * @author YvesCheung
 * 2021/12/2
 */
class ComposeHierarchyExtraInfoPlugin : HierarchyExtraInfoPlugin {

    override val uniqueKey: String = PluginKey

    override fun create(activity: Activity, targetLayer: Layer): Set<HierarchyExtraInfo> {
        return setOf(ComposeSourceLocationExtraInfo(activity))
    }
}