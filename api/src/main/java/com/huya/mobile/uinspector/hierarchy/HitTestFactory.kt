package com.huya.mobile.uinspector.hierarchy

import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * @author YvesCheung
 * 2021/2/1
 */
interface HitTestFactory : UInspectorPlugin {

    fun create(delegate: HitTest): HitTest

    companion object {

        fun create(): HitTest {
            var delegate: HitTest = AndroidHitTest()
            for (next in UInspector.plugins[HitTestFactory::class.java]) {
                delegate = next.create(delegate)
            }
            return delegate
        }
    }
}