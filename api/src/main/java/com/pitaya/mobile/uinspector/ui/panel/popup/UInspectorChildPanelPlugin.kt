package com.pitaya.mobile.uinspector.ui.panel.popup

import com.pitaya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * Plugins that used to create the panel that needs to be displayed on the popup window.
 *
 * @see UInspectorChildPanel
 *
 * @author YvesCheung
 * 2020/12/31
 */
interface UInspectorChildPanelPlugin : UInspectorPlugin {

    /**
     * To initialize your own [UInspectorChildPanel].
     * Load them into the framework.
     */
    fun createPanels(): Set<UInspectorChildPanel>
}