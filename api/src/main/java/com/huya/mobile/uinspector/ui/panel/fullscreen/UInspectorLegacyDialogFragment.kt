package com.huya.mobile.uinspector.ui.panel.fullscreen

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup


/**
 * @author YvesCheung
 * 2020/12/30
 */
internal class UInspectorLegacyDialogFragment : DialogFragment(), UInspectorPanel {

    private val delegate = UInspectorPanelDelegate()

    override fun show(activity: Activity) {
        show(activity.fragmentManager, "UInspectorLegacyDialogFragment")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, 0)
        isCancelable = false
        return delegate.onCreateDialog(super.onCreateDialog(savedInstanceState))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = delegate.onCreateView(inflater, container, savedInstanceState)

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
}