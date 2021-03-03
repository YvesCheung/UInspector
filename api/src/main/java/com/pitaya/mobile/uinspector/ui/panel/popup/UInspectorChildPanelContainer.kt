package com.pitaya.mobile.uinspector.ui.panel.popup

import android.view.View
import androidx.annotation.MainThread
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.yy.mobile.whisper.DeprecatedBy

/**
 * The container of [UInspectorChildPanel]
 *
 * @author YvesCheung
 * 2020/12/31
 */
@MainThread
interface UInspectorChildPanelContainer {

    @DeprecatedBy(replaceWith = "show(LayerFactory.create(%s))")
    fun show(anchorView: View)

    fun show(anchorView: Layer)

    fun dismiss()
}