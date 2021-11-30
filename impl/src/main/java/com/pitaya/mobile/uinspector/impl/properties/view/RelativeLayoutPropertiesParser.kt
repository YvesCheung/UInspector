package com.pitaya.mobile.uinspector.impl.properties.view

import android.os.Build
import android.view.Gravity
import android.widget.RelativeLayout
import com.pitaya.mobile.uinspector.util.gravityToString
import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/4
 */
open class RelativeLayoutPropertiesParser(view: RelativeLayout) : ViewGroupPropertiesParser<RelativeLayout>(view) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (view.gravity != Gravity.START or Gravity.TOP) {
                props["gravity"] = gravityToString(view.gravity)
            }
        }
    }
}