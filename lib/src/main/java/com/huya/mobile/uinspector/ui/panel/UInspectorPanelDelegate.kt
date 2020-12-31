package com.huya.mobile.uinspector.ui.panel

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.huya.mobile.uinspector.ui.UInspectorMask
import com.huya.mobile.uinspector.util.tryGetActivity

/**
 * @author YvesCheung
 * 2020/12/31
 */
@Suppress("UNUSED_PARAMETER")
internal class UInspectorPanelDelegate {

    fun onCreateDialog(dialog: Dialog): Dialog {
        dialog.window?.setDimAmount(0f)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saveInstance: Bundle?): View? {
        return UInspectorMask(inflater.context)
    }

    fun onStart(dialog: Dialog?) {
        val activity = tryGetActivity(dialog?.context)
        val rootView = activity?.findViewById<View>(android.R.id.content)
        if (rootView != null) {
            dialog?.window?.setLayout(rootView.width, rootView.height)
        } else {
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
}