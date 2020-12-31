package com.huya.mobile.uinspector.impl.properties

import android.os.Build
import android.view.View
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2020/12/31
 */
open class ViewPropertiesParser<T : View>(protected val view: T) {

    open fun parse(@Output props: MutableMap<String, Any?>) {
        props["id"] =
            if (view.id <= 0) "NO_ID"
            else "@+id/${view.context.resources.getResourceName(view.id)}"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            props["elevation"] = view.elevation
            props["z"] = view.z
        }
    }
}