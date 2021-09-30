package com.pitaya.mobile.uinspector.impl.hierarchy.extra

import android.content.Context
import android.os.Build
import android.text.SpannableStringBuilder
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.impl.R
import com.pitaya.mobile.uinspector.util.newLine
import com.pitaya.mobile.uinspector.util.resToString
import com.pitaya.mobile.uinspector.util.withColor

/**
 * @author YvesCheung
 * 2021/9/27
 */
@RequiresApi(Build.VERSION_CODES.Q)
open class TargetViewSourceLayoutIdInfo(private val context: Context) : HierarchyExtraInfo {

    @LayoutRes
    private var lastSourceId = -1

    override fun beforeHierarchy(index: Int, view: View, s: SpannableStringBuilder) {
        if (view.sourceLayoutResId > 0 && view.sourceLayoutResId != lastSourceId) {
            s.withColor(context, R.color.uinspector_view_layout_source_color) {
                newLine(index) {
                    append(resToString(context, view.sourceLayoutResId))
                }
            }
            lastSourceId = view.sourceLayoutResId
        }
    }
}