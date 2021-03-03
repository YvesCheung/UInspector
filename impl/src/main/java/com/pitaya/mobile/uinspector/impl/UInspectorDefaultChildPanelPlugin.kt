package com.pitaya.mobile.uinspector.impl

import com.pitaya.mobile.uinspector.impl.UInspectorDefaultPluginService.Companion.PLUGIN_KEY
import com.pitaya.mobile.uinspector.impl.accessibility.UInspectorAccessibilityPanel
import com.pitaya.mobile.uinspector.impl.hierarchy.UInspectorHierarchyPanel
import com.pitaya.mobile.uinspector.impl.properties.UInspectorPropertiesPanel
import com.pitaya.mobile.uinspector.impl.targets.UInspectorTargetsPanel
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelPlugin

/**
 * @author YvesCheung
 * 2021/2/2
 */
open class UInspectorDefaultChildPanelPlugin : UInspectorChildPanelPlugin {

    override val uniqueKey: String = PLUGIN_KEY

    override fun createPanels(): Set<UInspectorChildPanel> {
        return setOf(
            UInspectorPropertiesPanel(PROPERTIES_PRIORITY),
            UInspectorHierarchyPanel(HIERARCHY_PRIORITY),
            UInspectorTargetsPanel(TARGETS_PRIORITY),
            UInspectorAccessibilityPanel(ACCESSIBILITY_PRIORITY)
        )
    }

    companion object {

        const val PROPERTIES_PRIORITY = 100

        const val HIERARCHY_PRIORITY = 200

        const val TARGETS_PRIORITY = 300

        const val ACCESSIBILITY_PRIORITY = 400
    }
}