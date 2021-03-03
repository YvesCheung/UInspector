package com.pitaya.mobile.uinspector.ui.panel.popup

import android.content.Context
import android.view.View
import androidx.annotation.MainThread

/**
 * @author YvesCheung
 * 2020/12/31
 */
@MainThread
interface UInspectorChildPanel {

    /**
     * Determine the position of this panel
     */
    val priority: Int

    val title: CharSequence

    /**
     * Only called once when this panel is created on the screen
     */
    fun onCreateView(context: Context): View

    /**
     * Only called once when this panel is going to be destroyed
     */
    fun onDestroyView() {}

    /**
     * Called every time when the selected panel change
     */
    fun onUserVisibleHint(visible: Boolean) {}
}