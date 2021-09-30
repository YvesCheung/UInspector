package com.pitaya.mobile.uinspector.optional.multitype

import android.app.Activity
import android.content.Context
import android.text.SpannableStringBuilder
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfoPlugin
import com.pitaya.mobile.uinspector.optional.multitype.UInspectorMultiTypeService.Companion.PluginKey
import com.pitaya.mobile.uinspector.util.newLine
import com.pitaya.mobile.uinspector.util.withBold
import com.pitaya.mobile.uinspector.util.withColor
import com.pitaya.mobile.uinspector.util.canonicalName
import me.drakeet.multitype.MultiTypeAdapter

/**
 * @author YvesCheung
 * 2021/3/31
 */
class MultiTypeHierarchyPlugin(val context: Context) : HierarchyExtraInfoPlugin {

    override fun create(activity: Activity, targetView: View): Set<HierarchyExtraInfo> {
        return setOf(ViewBinderInfo(context))
    }

    override val uniqueKey: String = PluginKey

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
                                append(viewBinder.canonicalName)
                            }
                        }
                    }
                }
            }
        }
    }
}