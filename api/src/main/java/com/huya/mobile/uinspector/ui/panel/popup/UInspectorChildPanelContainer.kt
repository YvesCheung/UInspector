package com.huya.mobile.uinspector.ui.panel.popup

import android.view.View
import androidx.annotation.MainThread

/**
 * The container of [UInspectorChildPanel]
 *
 * @author YvesCheung
 * 2020/12/31
 */
@MainThread
interface UInspectorChildPanelContainer {

    fun show(anchorView: View)

    fun dismiss()
}