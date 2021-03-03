package com.pitaya.mobile.uinspector.hierarchy

import android.view.View
import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * @author YvesCheung
 * 2021/1/29
 */
interface LayerFactoryPlugin : UInspectorPlugin {

    fun create(view: View): Layer?

    companion object {

        fun create(view: View): Layer {
            for (factory in UInspector.plugins[LayerFactoryPlugin::class.java]) {
                val layer = factory.create(view)
                if (layer != null) return layer
            }
            return AndroidView(view)
        }
    }
}