package com.pitaya.mobile.uinspector.impl.properties.layoutParam

import android.view.Gravity.NO_GRAVITY
import android.view.View
import android.view.View.NO_ID
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import com.pitaya.mobile.uinspector.util.gravityToString
import com.pitaya.mobile.uinspector.util.idToString
import com.pitaya.mobile.uinspector.util.linkToView
import com.pitaya.mobile.uinspector.util.canonicalName
import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/4
 */
open class CoordinatorLayoutParamsPropertiesParser<P : LayoutParams>(val view: View, lp: P) :
    MarginLayoutParamsPropertiesParser<P>(lp) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        val behavior = lp.behavior
        if (behavior != null) {
            props["behavior"] = behavior.canonicalName
        }

        if (lp.gravity != NO_GRAVITY) {
            props["gravity"] = gravityToString(lp.gravity)
        }

        if (lp.anchorId != NO_ID) {
            props["anchorId"] =
                linkToView(idToString(view.context, lp.anchorId)) {
                    (view.parent as? View)?.findViewById(lp.anchorId)
                }

            if (lp.anchorGravity != NO_GRAVITY) {
                props["anchorGravity"] = gravityToString(lp.anchorGravity)
            }
        }

        if (lp.keyline != -1
            && lp.anchorId == NO_ID /*If an anchor is present the keyline will be ignored*/) {
            props["keyline"] = lp.keyline
        }
    }
}