package com.huya.mobile.uinspector.impl

import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.hierarchy.AndroidView
import com.huya.mobile.uinspector.hierarchy.Layer
import com.huya.mobile.uinspector.impl.accessibility.UInspectorAccessibilityPanel
import com.huya.mobile.uinspector.impl.hierarchy.UInspectorHierarchyPanel
import com.huya.mobile.uinspector.impl.properties.UInspectorPropertiesPanel
import com.huya.mobile.uinspector.impl.targets.UInspectorTargetsPanel
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelService

/**
 * @author YvesCheung
 * 2021/1/2
 */
class UInspectorDefaultPanelService : UInspectorChildPanelService {

    override fun createPanels(): Set<UInspectorChildPanel> {
        val target: Layer? =
            UInspector.currentState.withLifecycle?.lastTargetViews?.lastOrNull()
        return if (target is AndroidView) {
            setOf(
                UInspectorPropertiesPanel(PROPERTIES_PRIORITY),
                UInspectorHierarchyPanel(HIERARCHY_PRIORITY),
                UInspectorTargetsPanel(TARGETS_PRIORITY),
                UInspectorAccessibilityPanel(ACCESSIBILITY_PRIORITY)
            )
        } else {
            setOf(
                UInspectorHierarchyPanel(HIERARCHY_PRIORITY),
                UInspectorTargetsPanel(TARGETS_PRIORITY)
            )
        }
    }

    companion object {

        const val PROPERTIES_PRIORITY = 100

        const val HIERARCHY_PRIORITY = 200

        const val TARGETS_PRIORITY = 300

        const val ACCESSIBILITY_PRIORITY = 400
    }
}