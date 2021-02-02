package com.huya.mobile.uinspector.optional.compose.properties

import android.content.Context
import android.view.View
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel

/**
 * @author YvesCheung
 * 2021/2/2
 */
class UInspectorComposePropertiesPanel(override val priority: Int) : UInspectorChildPanel {

    override val title = "Properties"

    override fun onCreateView(context: Context): View {
        TODO("Not yet implemented")
    }
}