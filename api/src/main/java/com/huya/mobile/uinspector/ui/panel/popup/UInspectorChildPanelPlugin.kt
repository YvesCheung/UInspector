package com.huya.mobile.uinspector.ui.panel.popup

import com.huya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * Using Java SPI mechanism
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