package com.huya.mobile.uinspector.ui.panel

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

/**
 * @author YvesCheung
 * 2020/12/30
 */
internal class UInspectorDialogFragment : DialogFragment(), UInspectorPanel {

    private val delegate = UInspectorPanelDelegate()

    override fun show(activity: Activity) {
        show((activity as FragmentActivity).supportFragmentManager, "UInspectorDialogFragment")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, 0)
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