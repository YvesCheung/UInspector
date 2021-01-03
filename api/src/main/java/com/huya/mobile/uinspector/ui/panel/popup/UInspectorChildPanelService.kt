package com.huya.mobile.uinspector.ui.panel.popup

/**
 * Using Java SPI mechanism
 *
 * @author YvesCheung
 * 2020/12/31
 */
interface UInspectorChildPanelService {

    /**
     * To initialize your own [UInspectorChildPanel].
     * Load them into the framework.
     */
    fun createPanels(): Set<UInspectorChildPanel>
}