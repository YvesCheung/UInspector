package com.huya.mobile.uinspector.impl.properties

import android.os.Build
import android.widget.ImageView
import com.huya.mobile.uinspector.impl.utils.colorToString
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

            if (view.cropToPadding) {
                props["cropToPadding"] = view.cropToPadding
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val tint = view.imageTintList
            if (tint != null) {
                props["tint"] = colorToString(tint)
            }
        }

        if (view.baseline > 0) {
            props["baseline"] = view.baseline
        }

        if (view.baselineAlignBottom) {
            props["baselineAlignBottom"] = view.baselineAlignBottom
        }
    }
}