package com.huya.mobile.uinspector.impl

import com.huya.mobile.uinspector.impl.hierarchy.UInspectorHierarchyPanel
import com.huya.mobile.uinspector.impl.properties.UInspectorPropertiesPanel
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelService

/**
 * @author YvesCheung
 * 2021/1/2
 */
class UInspectorDefaultPanelService : UInspectorChildPanelService {

    override val panels: Set<UInspectorChildPanel> = setOf(
        UInspectorPropertiesPanel(PROPERTIES_PRIORITY),
        UInspectorHierarchyPanel(HIERARCHY_PRIORITY)
    )

    companion object {

        const val PROPERTIES_PRIORITY = 100

        const val HIERARCHY_PRIORITY = 200
    }
}