package com.huya.mobile.uinspector.optional.compose.properties

import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.hierarchy.Layer
import com.huya.mobile.uinspector.impl.UInspectorDefaultChildPanelPlugin
import com.huya.mobile.uinspector.impl.hierarchy.UInspectorHierarchyPanel
import com.huya.mobile.uinspector.impl.targets.UInspectorTargetsPanel
import com.huya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel

/**
 * @author YvesCheung
 * 2021/2/2
 */
class UInspectorComposeChildPanelPlugin : UInspectorDefaultChildPanelPlugin() {

    override fun createPanels(): Set<UInspectorChildPanel> {
        val target: Layer? =
            UInspector.currentState.withLifecycle?.lastTargetViews?.lastOrNull()
        if (target is ComposeView) {
            return setOf(
                UInspectorHierarchyPanel(HIERARCHY_PRIORITY),
                UInspectorTargetsPanel(TARGETS_PRIORITY)
            )
        } else {
            return super.createPanels()
        }
    }
}