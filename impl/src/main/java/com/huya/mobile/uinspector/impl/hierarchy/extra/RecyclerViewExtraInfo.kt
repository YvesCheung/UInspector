package com.huya.mobile.uinspector.impl.hierarchy.extra

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.huya.mobile.uinspector.impl.hierarchy.UInspectorHierarchyPanel.Companion.newLine
import com.huya.mobile.uinspector.impl.hierarchy.UInspectorHierarchyPanel.Companion.withColor

/**
 * @author YvesCheung
 * 2021/1/6
 */
open class RecyclerViewExtraInfo(private val context: Context) : HierarchyExtraInfo {

    override fun afterHierarchy(index: Int, view: View, s: SpannableStringBuilder) {
        if (view is RecyclerView) {
            val adapter = view.adapter
            if (adapter != null) {
                s.withColor(context) {
                    newLine(index) {
                        append(adapter::class.java.canonicalName)
                    }
                }
            }
        }
    }
}