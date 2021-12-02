package com.pitaya.mobile.uinspector.optional.compose.hirarchy.extra

import android.app.Activity
import android.text.SpannableStringBuilder
import androidx.compose.ui.tooling.data.UiToolingDataApi
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.util.newLine
import com.pitaya.mobile.uinspector.util.withColor

/**
 * @author YvesCheung
 * 2021/12/2
 */
class ComposeSourceLocationExtraInfo(private val activity: Activity) : HierarchyExtraInfo {

    @OptIn(UiToolingDataApi::class)
    override fun afterHierarchy(index: Int, layer: Layer, s: SpannableStringBuilder) {
        if (layer is ComposeView) {
            val location = layer.sourceCode
            if (location != null) {
                s.withColor(activity) {
                    newLine(index) {
                        append(location.toString())
                    }
                }
            }
        }
    }
}