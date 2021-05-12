package com.huya.mobile.uinspector.optional.multitype

import android.app.Activity
import android.content.Context
import android.text.SpannableStringBuilder
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.auto.service.AutoService
import com.huya.mobile.uinspector.impl.hierarchy.extra.HierarchyExtraInfo
import com.huya.mobile.uinspector.impl.hierarchy.extra.HierarchyExtraInfoService
import com.huya.mobile.uinspector.util.newLine
import com.huya.mobile.uinspector.util.withBold
import com.huya.mobile.uinspector.util.withColor
import me.drakeet.multitype.MultiTypeAdapter

/**
 * @author YvesCheung
 * 2021/4/1
 */
@AutoService(HierarchyExtraInfoService::class)
class MultiTypeExtraInfoService : HierarchyExtraInfoService {

    override fun create(activity: Activity, targetView: View): Set<HierarchyExtraInfo> {
        return setOf(ViewBinderInfo(activity))
    }

    private class ViewBinderInfo(val context: Context) : HierarchyExtraInfo {

        override fun beforeHierarchy(index: Int, view: View, s: SpannableStringBuilder) {
            val maybeRecyclerView = view.parent
            if (maybeRecyclerView is RecyclerView) {
                val layoutManager = maybeRecyclerView.layoutManager ?: return
                val maybeMultiTypeAdapter = maybeRecyclerView.adapter
                if (maybeMultiTypeAdapter is MultiTypeAdapter) {
                    val viewBinder = maybeMultiTypeAdapter.typePool
                        .getItemViewBinder(layoutManager.getItemViewType(view))

                    s.withColor(context) {
                        withBold {
                            newLine(index) {
                                append(
                                    viewBinder::class.java.canonicalName
                                        ?: viewBinder::class.java.name
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}