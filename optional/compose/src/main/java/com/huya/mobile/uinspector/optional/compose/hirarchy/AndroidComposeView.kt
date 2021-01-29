package com.huya.mobile.uinspector.optional.compose.hirarchy

import android.view.View
import com.huya.mobile.uinspector.hierarchy.AndroidView
import com.huya.mobile.uinspector.hierarchy.Layer
import com.huya.mobile.uinspector.optional.compose.inspect.ComposeInspector

/**
 * @author YvesCheung
 * 2021/1/29
 */
open class AndroidComposeView(view: View) : AndroidView(view) {

    override val children: Sequence<Layer>
        get() = super.children +
            (ComposeInspector.tryGetLayoutInfos(view)
                ?.map { ComposeView(this, it) }
                ?: emptySequence())
}