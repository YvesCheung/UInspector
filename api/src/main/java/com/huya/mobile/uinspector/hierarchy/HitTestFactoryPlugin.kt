package com.huya.mobile.uinspector.hierarchy

import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * It is used to provide plugins to find the UI layer that is 'hit' by the user input
 * when touch event occurs
 *
 * @author YvesCheung
 * 2021/2/1
 */
interface HitTestFactoryPlugin : UInspectorPlugin {

    /**
     * @param delegate Gestures that cannot be processed can be delegated to it
     */
    fun create(delegate: HitTest): HitTest

    companion object {

        fun create(): HitTest {
            var delegate: HitTest = AndroidHitTest()
            for (next in UInspector.plugins[HitTestFactoryPlugin::class.java]) {
                delegate = next.create(delegate)
            }
            return delegate
        }
    }
}