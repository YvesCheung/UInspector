package com.huya.mobile.uinspector.hierarchy

import android.view.View
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * @author YvesCheung
 * 2021/1/29
 */
interface LayerFactory : UInspectorPlugin {

    fun create(view: View): Layer?

    companion object {

        fun create(view: View): Layer {
            for (factory in UInspector.plugins[LayerFactory::class.java]) {
                val layer = factory.create(view)
                if (layer != null) return layer
            }
            return AndroidView(view)
        }
    }
}