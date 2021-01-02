package com.huya.mobile.uinspector.ui.panel.popup

import android.content.Context
import android.view.View
import androidx.annotation.MainThread

/**
 * @author YvesCheung
 * 2020/12/31
 */
@MainThread
interface UInspectorChildPanel {

    val priority: Int

    val title: CharSequence

    fun onCreateView(context: Context): View

    fun onDestroyView() {}

    fun onUserVisibleHint(visible: Boolean) {}
}