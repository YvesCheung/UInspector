package com.pitaya.mobile.uinspector.ui.panel.fullscreen

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.lifecycle.Disposable
import com.pitaya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelContainer


/**
 * @author YvesCheung
 * 2020/12/30
 */
internal class UInspectorLegacyDialogFragment : DialogFragment(), UInspectorPanel {

    private val delegate = UInspectorPanelDelegate()

    override val childPanelContainer: UInspectorChildPanelContainer?
        get() = delegate.childPanelContainer

    override fun show(activity: Activity) {
        show(activity.fragmentManager, "UInspectorLegacyDialogFragment")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, 0)
        isCancelable = false
        return delegate.onCreateDialog(requireNotNull(activity), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = delegate.onCreateView(inflater.context)

    override fun onStart() {
        super.onStart()
        delegate.onStart(dialog)
    }

    override fun close() {
        try {
            dismissAllowingStateLoss()
        } catch (ignore: Throwable) {
        }
    }

    override fun updateTargetView(view: View) = delegate.updateTargetView(view)

    override fun updateTargetViews(views: List<View>) = delegate.updateTargetViews(views)

    override fun updateTargetLayer(layer: Layer) = delegate.updateTargetLayer(layer)

    override fun updateTargetLayers(layers: List<Layer>) = delegate.updateTargetLayers(layers)

    override fun addDecoration(decoration: UInspectorDecoration): Disposable =
        delegate.addDecoration(decoration)

    override fun removeDecoration(decoration: UInspectorDecoration) =
        delegate.removeDecoration(decoration)

    override fun invalidate() = delegate.invalidateMask()
}