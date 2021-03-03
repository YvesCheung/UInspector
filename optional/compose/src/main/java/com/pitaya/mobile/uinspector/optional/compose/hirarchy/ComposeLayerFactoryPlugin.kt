package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import android.view.View
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.hierarchy.LayerFactoryPlugin
import com.pitaya.mobile.uinspector.optional.compose.UInspectorComposeService.Companion.PluginKey
import com.pitaya.mobile.uinspector.optional.compose.inspect.mightBeComposeView

/**
 * @author YvesCheung
 * 2021/1/29
 */
class ComposeLayerFactoryPlugin : LayerFactoryPlugin {

    override val uniqueKey: String = PluginKey

    override fun create(view: View): Layer? {
        if (view.mightBeComposeView) {
            return AndroidComposeView(view)
        }
        return null
    }
}