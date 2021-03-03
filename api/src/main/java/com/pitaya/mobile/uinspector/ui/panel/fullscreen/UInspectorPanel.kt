package com.pitaya.mobile.uinspector.ui.panel.fullscreen

import android.app.Activity
import android.view.InputEvent
import android.view.View
import androidx.annotation.MainThread
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.lifecycle.Disposable
import com.pitaya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelContainer

/**
 * Fullscreen panel, handle all [InputEvent].
 *
 * @author YvesCheung
 * 2020/12/30
 */
@MainThread
interface UInspectorPanel {

    val childPanelContainer: UInspectorChildPanelContainer?

    fun show(activity: Activity)

    fun updateTargetViews(views: List<View>)

    fun updateTargetView(view: View)

    fun updateTargetLayers(layers: List<Layer>)

    fun updateTargetLayer(layer: Layer)

    fun addDecoration(decoration: UInspectorDecoration): Disposable

    fun removeDecoration(decoration: UInspectorDecoration)

    fun invalidate()

    fun close()
}