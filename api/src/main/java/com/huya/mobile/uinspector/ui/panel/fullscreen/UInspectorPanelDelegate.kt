package com.huya.mobile.uinspector.ui.panel.fullscreen

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.huya.mobile.uinspector.hierarchy.ViewHierarchy
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorPopupPanelContainerImpl

/**
 * @author YvesCheung
 * 2020/12/31
 */
@Suppress("UNUSED_PARAMETER")
internal class UInspectorPanelDelegate {

    var mask: UInspectorMask? = null

    val childPanelContainer: UInspectorPopupPanelContainerImpl?
        get() = mask?.popupPanelContainer

    fun updateTargetViews(views: List<View>) {
        mask?.updateTargetViews(views)
    }

    fun updateTargetView(view: View) {
        mask?.updateTargetViews(ViewHierarchy.get(view))
    }

    fun onCreateDialog(context: Context, theme: Int): Dialog {
        val dialog = UInspectorKeyEventDispatcher(context, theme)
        dialog.onKeyEvent = { keyEvent ->
            mask?.dispatchKeyEvent(keyEvent) ?: false
        }
        dialog.window?.setDimAmount(0f)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    fun onCreateView(context: Context): View {
        return UInspectorMask(context).also { mask = it }
    }

    fun onStart(dialog: Dialog?) {
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }
}