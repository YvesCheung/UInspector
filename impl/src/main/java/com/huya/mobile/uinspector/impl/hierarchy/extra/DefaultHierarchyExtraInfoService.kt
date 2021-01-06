package com.huya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.view.View

/**
 * @author YvesCheung
 * 2021/1/6
 */
class DefaultHierarchyExtraInfoService : HierarchyExtraInfoService {

    override fun create(activity: Activity, targetView: View): Set<HierarchyExtraInfo> = setOf(
        HierarchyActivityInfo(activity),
        HierarchyFragmentInfo(activity),
        RecyclerViewExtraInfo(activity)
    )
}