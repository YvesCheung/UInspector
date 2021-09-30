package com.huya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.os.Build
import android.view.View

/**
 * @author YvesCheung
 * 2021/1/6
 */
class DefaultHierarchyExtraInfoService : HierarchyExtraInfoService {

    override fun create(activity: Activity, targetView: View): Set<HierarchyExtraInfo> {
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

}