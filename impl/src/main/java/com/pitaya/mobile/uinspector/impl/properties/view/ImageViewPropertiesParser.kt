package com.pitaya.mobile.uinspector.impl.properties.view

import android.os.Build
import android.widget.ImageView
import com.pitaya.mobile.uinspector.util.colorToString
import com.pitaya.mobile.uinspector.util.drawableToString
import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/2
 */
open class ImageViewPropertiesParser(view: ImageView) : ViewPropertiesParser<ImageView>(view) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        props["scaleType"] = view.scaleType

        if (view.drawable != null) {
            props["src"] = drawableToString(view.drawable)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            if (view.imageAlpha != 255) {
                props["imageAlpha"] = view.imageAlpha
            }

            if (view.cropToPadding) {
                props["cropToPadding"] = view.cropToPadding
            }

            if (view.adjustViewBounds) {
                props["adjustViewBounds"] = view.adjustViewBounds
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val tint = view.imageTintList
            if (tint != null) {
                props["imageTint"] = colorToString(tint)
            }

            if (view.imageTintMode != null) {
                props["imageTintMode"] = view.imageTintMode
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