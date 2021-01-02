package com.huya.mobile.uinspector.impl.properties

import android.graphics.Rect
import android.os.Build
import android.view.View
import com.huya.mobile.uinspector.impl.utils.dpStr
import com.huya.mobile.uinspector.impl.utils.drawableToString
import com.huya.mobile.uinspector.impl.utils.idToString
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2020/12/31
 */
open class ViewPropertiesParser<T : View>(protected val view: T) {

    open fun parse(@Output props: MutableMap<String, Any?>) {
        props["id"] =
            if (view.id <= 0) "NO_ID"
            else idToString(view.context, view.id)

        props["rect"] = Rect(view.left, view.top, view.right, view.bottom)

        if (view.background != null) {
            props["background"] = drawableToString(view.background)
        }

        if (view.scrollX != 0) {
            props["scrollX"] = view.scrollX
        }

        if (view.scrollY != 0) {
            props["scrollY"] = view.scrollY
        }

        if (view.translationX != 0f) {
            props["translationX"] = view.translationX
        }

        if (view.translationY != 0f) {
            props["translationY"] = view.translationY
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (view.translationZ != 0f) {
                props["translationZ"] = view.translationZ
            }

            if (view.elevation != 0f) {
                props["elevation"] = view.elevation.dpStr
            }
        }

        if (view.scaleX != 1f) {
            props["scaleX"] = view.scaleX
        }

        if (view.scaleY != 1f) {
            props["scaleY"] = view.scaleY
        }
    }
}