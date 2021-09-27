package com.huya.mobile.uinspector.impl.hierarchy.extra

import android.content.Context
import android.os.Build
import android.text.SpannableStringBuilder
import android.view.View
import androidx.annotation.RequiresApi
import com.huya.mobile.uinspector.impl.R
import com.huya.mobile.uinspector.util.newLine
import com.huya.mobile.uinspector.util.resToString
import com.huya.mobile.uinspector.util.withColor

/**
 * @author YvesCheung
 * 2021/9/27
 */
@RequiresApi(Build.VERSION_CODES.Q)
class TargetViewSourceLayoutIdInfo(private val context: Context, private val target: View) :
    HierarchyExtraInfo {

    override fun beforeHierarchy(index: Int, view: View, s: SpannableStringBuilder) {
        if (target === view) {
            s.withColor(context, R.color.uinspector_view_layout_source_color) {
                newLine(index) {
                    append(resToString(context, view.sourceLayoutResId))
                }
            }
        }
    }
}