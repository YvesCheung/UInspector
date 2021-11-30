package com.pitaya.mobile.uinspector.ui.panel.popup

import android.view.View
import androidx.annotation.MainThread
import com.github.yvescheung.whisper.DeprecatedBy
import com.pitaya.mobile.uinspector.hierarchy.Layer

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