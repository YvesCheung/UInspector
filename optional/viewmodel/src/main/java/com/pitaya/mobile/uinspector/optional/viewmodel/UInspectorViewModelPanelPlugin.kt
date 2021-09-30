package com.pitaya.mobile.uinspector.optional.viewmodel

import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelPlugin

/**
 * @author YvesCheung
 * 2021/9/30
 */
class UInspectorViewModelPanelPlugin(override val uniqueKey: String) : UInspectorChildPanelPlugin {

    override fun createPanels(): Set<UInspectorChildPanel> {
        return setOf(UInspectorViewModelPanel(VIEWMODEL_PRIORITY))
    }

    companion object {

        const val VIEWMODEL_PRIORITY = 350
    }
}