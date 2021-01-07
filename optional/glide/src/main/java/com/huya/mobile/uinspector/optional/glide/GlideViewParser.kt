package com.huya.mobile.uinspector.optional.glide

import android.view.View
import com.bumptech.glide.request.Request
import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParser

/**
 * @author YvesCheung
 * 2021/1/7
 */
class GlideViewParser(view: View, private val request: Request) : ViewPropertiesParser<View>(view) {

    override fun parse(props: MutableMap<String, Any?>) {
        super.parse(props)
        GlideRequestParser(view.context, request).parse(props)
    }
}