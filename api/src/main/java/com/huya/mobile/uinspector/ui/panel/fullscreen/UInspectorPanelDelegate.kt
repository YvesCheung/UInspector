package com.huya.mobile.uinspector.ui.panel.fullscreen

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.huya.mobile.uinspector.hierarchy.WindowManager
import com.huya.mobile.uinspector.util.tryGetActivity

/**
 * @author YvesCheung
 * 2020/12/31
 */
@Suppress("UNUSED_PARAMETER")
internal class UInspectorPanelDelegate {

    private var mask: UInspectorMask? = null

    fun onCreateDialog(context: Context, theme: Int): Dialog {
        val dialog = UInspectorKeyEventDispatcher(context, theme)
        dialog.onKeyEvent = { keyEvent ->
            val activity = tryGetActivity(context)
            val excludeDecorView = mask?.currentDecorView
            var handle = false
            if (activity != null) {
                val decorViews = WindowManager.findDecorViews(activity)
                for (decor in decorViews.asReversed()) {
                    if (excludeDecorView === decor) {
                        continue
                    }

                    handle = decor.dispatchKeyEvent(keyEvent)
                    break
                }
            }
            handle
        }
        dialog.window?.setDimAmount(0f)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saveIns: Bundle?): View? {
        return UInspectorMask(inflater.context).also { mask = it }
    }

    fun onStart(dialog: Dialog?) {
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }
}