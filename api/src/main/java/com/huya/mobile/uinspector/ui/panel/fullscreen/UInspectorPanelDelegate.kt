package com.huya.mobile.uinspector.ui.panel.fullscreen

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

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

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saveIns: Bundle?): View? =
        UInspectorMask(inflater.context)

    fun onStart(dialog: Dialog?) {
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }
}