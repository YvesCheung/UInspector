package com.huya.mobile.uinspector.impl.properties

import android.os.Build
import android.widget.ImageView
import com.huya.mobile.uinspector.impl.utils.drawableToString

/**
 * @author YvesCheung
 * 2021/1/2
 */
class ImageViewPropertiesParser(view: ImageView) : ViewPropertiesParser<ImageView>(view) {

    override fun parse(props: MutableMap<String, Any?>) {
        super.parse(props)

        props["scaleType"] = view.scaleType

        props["imageMatrix"] = view.imageMatrix.toShortString()

        if (view.contentDescription != null) {
            props["contentDescription"] = view.contentDescription
        }

        if (view.drawable != null) {
            props["drawable"] = drawableToString(view.drawable)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            if (view.imageAlpha != 255) {
                props["imageAlpha"] = view.imageAlpha
            }
        }
    }
}