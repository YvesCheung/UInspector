package com.huya.mobile.uinspector.ui.panel

import android.app.Activity

/**
 * @author YvesCheung
 * 2020/12/30
 */
internal interface UInspectorPanel {

    fun show(activity: Activity)

    fun close()
}