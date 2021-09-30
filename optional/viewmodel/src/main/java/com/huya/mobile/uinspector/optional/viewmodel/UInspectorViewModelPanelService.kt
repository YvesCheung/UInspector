package com.huya.mobile.uinspector.optional.viewmodel

import com.google.auto.service.AutoService
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelService

/**
 * @author YvesCheung
 * 2021/9/30
 */
@AutoService(UInspectorChildPanelService::class)
class UInspectorViewModelPanelService : UInspectorChildPanelService {

    override fun createPanels(): Set<UInspectorChildPanel> {
        return setOf(UInspectorViewModelPanel(VIEWMODEL_PRIORITY))
    }

    companion object {

        const val VIEWMODEL_PRIORITY = 350
    }
}