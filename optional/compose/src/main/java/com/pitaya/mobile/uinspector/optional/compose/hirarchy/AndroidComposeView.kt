package com.pitaya.mobile.uinspector.optional.compose.hirarchy

import android.view.View
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.inspect.ComposeInspector.parseChildren

/**
 * @author YvesCheung
 * 2021/1/29
 */
open class AndroidComposeView(view: View) : AndroidView(view) {

    override val children: Sequence<Layer>
        get() = parseChildren(this)
}