package com.huya.mobile.uinspector.ui.panel

import android.app.Activity
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huya.mobile.uinspector.ui.UInspectorMask


/**
 * @author YvesCheung
 * 2020/12/30
 */
internal class UInspectorLegacyDialogFragment : DialogFragment(), UInspectorPanel {

    override fun show(activity: Activity) {
        show(activity.fragmentManager, "UInspectorLegacyDialogFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return UInspectorMask(inflater.context)
    }

    override fun close() {
        try {
            dismissAllowingStateLoss()
        } catch (ignore: Throwable) {
        }
    }
}