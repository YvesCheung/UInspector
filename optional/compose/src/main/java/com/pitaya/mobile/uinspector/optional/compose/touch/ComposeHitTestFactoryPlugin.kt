package com.pitaya.mobile.uinspector.optional.compose.touch

import com.pitaya.mobile.uinspector.hierarchy.HitTest
import com.pitaya.mobile.uinspector.hierarchy.HitTestFactoryPlugin
import com.pitaya.mobile.uinspector.optional.compose.UInspectorComposeService.Companion.PLUGIN_KEY

/**
 * @author YvesCheung
 * 2021/2/1
 */
class ComposeHitTestFactoryPlugin : HitTestFactoryPlugin {

    override val uniqueKey: String = PLUGIN_KEY

    override fun create(delegate: HitTest): HitTest = ComposeHitTest(delegate)
}