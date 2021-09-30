package com.pitaya.mobile.uinspector.impl.hierarchy.extra

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.util.newLine
import com.pitaya.mobile.uinspector.util.withColor
import com.pitaya.mobile.uinspector.util.canonicalName

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
                        append(adapter.canonicalName)
                    }
                }
            }
        }
    }
}