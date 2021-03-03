package com.pitaya.mobile.uinspector.impl.hierarchy.extra

import android.text.SpannableStringBuilder
import android.view.View
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.yy.mobile.whisper.Output

/**
 * @see HierarchyExtraInfoService
 *
 * @author YvesCheung
 * 2021/1/6
 */
interface HierarchyExtraInfo {

    fun beforeHierarchy(index: Int, view: View, @Output s: SpannableStringBuilder) {}

    fun beforeHierarchy(index: Int, layer: Layer, @Output s: SpannableStringBuilder) {
        if (layer is AndroidView) {
            beforeHierarchy(index, layer.view, s)
        }
    }

    fun afterHierarchy(index: Int, view: View, @Output s: SpannableStringBuilder) {}

    fun afterHierarchy(index: Int, layer: Layer, @Output s: SpannableStringBuilder) {
        if (layer is AndroidView) {
            afterHierarchy(index, layer.view, s)
        }
    }
}