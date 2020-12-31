package com.huya.mobile.uinspector.ui.panel.popup

import android.view.View
import android.view.ViewGroup

/**
 * @author YvesCheung
 * 2020/12/31
 */
interface UInspectorChildPanel {

    val title: CharSequence

    fun onCreateView(container: ViewGroup): View

    fun onUserVisibleHint(visible: Boolean)
}