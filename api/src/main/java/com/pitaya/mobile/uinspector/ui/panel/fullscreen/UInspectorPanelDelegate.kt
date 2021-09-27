package com.pitaya.mobile.uinspector.ui.panel.fullscreen

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.MainThread
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.hierarchy.LayerFactoryPlugin
import com.pitaya.mobile.uinspector.hierarchy.ViewHierarchy
import com.pitaya.mobile.uinspector.lifecycle.Disposable
import com.pitaya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorPopupPanelContainerImpl

/**
 * @author YvesCheung
 * 2020/12/31
 */
@Suppress("UNUSED_PARAMETER")
internal class UInspectorPanelDelegate {

    var mask: UInspectorMask? = null

    val childPanelContainer: UInspectorPopupPanelContainerImpl?
        @MainThread
        get() = mask?.popupPanelContainer

    @MainThread
    fun updateTargetViews(views: List<View>) {
        mask?.updateTargetViews(views)
    }

    @MainThread
    fun updateTargetView(view: View) {
        updateTargetLayer(LayerFactoryPlugin.create(view))
    }

    @MainThread
    fun updateTargetLayers(layers: List<Layer>) {
        mask?.updateTargetLayers(layers)
    }

    @MainThread
    fun updateTargetLayer(layer: Layer) {
        mask?.updateTargetLayers(ViewHierarchy.get(layer))
    }

    @MainThread
    fun addDecoration(decoration: UInspectorDecoration): Disposable {
        //todo: if mask == null, add to a waiting queue?
        return mask?.addDecoration(decoration) ?: Disposable.EMPTY
    }

    @MainThread
    fun removeDecoration(decoration: UInspectorDecoration) {
        mask?.removeDecoration(decoration)
    }

    @MainThread
    fun invalidateMask() {
        mask?.invalidate()
    }

    @MainThread
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

    @MainThread
    fun onCreateView(context: Context): View {
        return UInspectorMask(context).also { mask = it }
    }

    @MainThread
    fun onStart(dialog: Dialog?) {
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            dialog?.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}