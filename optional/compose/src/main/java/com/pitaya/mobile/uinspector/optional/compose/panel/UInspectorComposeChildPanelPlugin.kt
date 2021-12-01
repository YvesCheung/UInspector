package com.pitaya.mobile.uinspector.optional.compose.panel

import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.impl.UInspectorDefaultChildPanelPlugin
import com.pitaya.mobile.uinspector.impl.hierarchy.UInspectorHierarchyPanel
import com.pitaya.mobile.uinspector.impl.targets.UInspectorTargetsPanel
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.SubComposition
import com.pitaya.mobile.uinspector.optional.compose.properties.UInspectorComposePropertiesPanel
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel

/**
 * @author YvesCheung
 * 2021/2/2
 */
class UInspectorComposeChildPanelPlugin : UInspectorDefaultChildPanelPlugin() {

    override fun createPanels(): Set<UInspectorChildPanel> {
        val target: Layer? =
            UInspector.currentState.withLifecycle?.lastTargetViews?.lastOrNull()
        return if (target is ComposeView || target is SubComposition) {
            setOf(
                UInspectorComposePropertiesPanel(PROPERTIES_PRIORITY),
                UInspectorHierarchyPanel(HIERARCHY_PRIORITY),
                UInspectorTargetsPanel(TARGETS_PRIORITY)
            )
        } else {
            super.createPanels()
        }
    }
}