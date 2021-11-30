package com.pitaya.mobile.uinspector.optional.glide

import android.view.View
import com.bumptech.glide.request.Request
import com.pitaya.mobile.uinspector.impl.properties.view.ViewPropertiesParser
import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/7
 */
open class GlideViewParser(view: View, private val request: Request) : ViewPropertiesParser<View>(view) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)
        GlideRequestParser(view.context, request).parse(props)
    }
}