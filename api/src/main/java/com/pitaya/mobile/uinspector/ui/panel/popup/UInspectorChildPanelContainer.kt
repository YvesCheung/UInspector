package com.pitaya.mobile.uinspector.ui.panel.popup

import android.view.View
import androidx.annotation.MainThread
import com.pitaya.mobile.uinspector.hierarchy.Layer

/**
 * The container of [UInspectorChildPanel]
 *
 * @author YvesCheung
 * 2020/12/31
 */
@MainThread
interface UInspectorChildPanelContainer {

    @Deprecated("Use show(LayerFactory.create()) instead")
    fun show(anchorView: View)

    fun show(anchorView: Layer)

    fun dismiss()
}