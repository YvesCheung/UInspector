package com.huya.mobile.uinspector.optional.compose

import android.util.Log
import android.view.View
import com.huya.mobile.uinspector.hierarchy.AndroidHitTest
import com.huya.mobile.uinspector.hierarchy.Layer
import com.huya.mobile.uinspector.hierarchy.LayerFactoryService
import com.huya.mobile.uinspector.hierarchy.TouchTargets
import com.huya.mobile.uinspector.optional.compose.hirarchy.AndroidComposeView
import com.huya.mobile.uinspector.optional.compose.hirarchy.ComposeHitTest
import com.huya.mobile.uinspector.optional.compose.inspect.mightBeComposeView

/**
 * @author YvesCheung
 * 2021/1/29
 */
class ComposeLayerFactory : LayerFactoryService {

    private val hitTestField by lazy(LazyThreadSafetyMode.NONE) {
        val f = TouchTargets::class.java.getDeclaredField("hitTest")
        f.isAccessible = true
        f
    }

    init {
        try {
            hitTestField.set(null, ComposeHitTest(AndroidHitTest()))
        } catch (e: Throwable) {
            Log.e("UInspector", e.toString())
        }
    }

    override fun create(view: View): Layer? {
        if (view.mightBeComposeView) {
            return AndroidComposeView(view)
        }
        return null
    }
}