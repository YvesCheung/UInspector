package com.huya.mobile.uinspector.ui.panel.popup

import android.content.Context
import android.view.View

/**
 * @author YvesCheung
 * 2020/12/31
 */
interface UInspectorChildPanel {

    val priority: Int

    val title: CharSequence

    fun onCreateView(context: Context): View

    fun onUserVisibleHint(visible: Boolean) {}
}