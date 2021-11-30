package com.pitaya.mobile.uinspector.impl.properties.layoutParam

import android.os.Build
import android.view.View
import android.widget.RelativeLayout.*
import androidx.annotation.RequiresApi
import com.pitaya.mobile.uinspector.util.idToString
import com.pitaya.mobile.uinspector.util.linkToView
import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/4
 */
open class RelativeLayoutParamsPropertiesParser<P : LayoutParams>(val view: View, lp: P) :
    MarginLayoutParamsPropertiesParser<P>(lp) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkRule(lp, "above", ABOVE, props)
            checkRule(lp, "alignBaseline", ALIGN_BASELINE, props)
            checkRule(lp, "alignBottom", ALIGN_BOTTOM, props)
            checkRule(lp, "alignLeft", ALIGN_LEFT, props)
            checkRule(lp, "alignParentBottom", ALIGN_PARENT_BOTTOM, props)
            checkRule(lp, "alignParentLeft", ALIGN_PARENT_LEFT, props)
            checkRule(lp, "alignParentRight", ALIGN_PARENT_RIGHT, props)
            checkRule(lp, "alignParentTop", ALIGN_PARENT_TOP, props)
            checkRule(lp, "alignRight", ALIGN_RIGHT, props)
            checkRule(lp, "alignTop", ALIGN_TOP, props)
            checkRule(lp, "below", BELOW, props)
            checkRule(lp, "centerHorizontal", CENTER_HORIZONTAL, props)
            checkRule(lp, "center", CENTER_IN_PARENT, props)
            checkRule(lp, "centerVertical", CENTER_VERTICAL, props)
            checkRule(lp, "leftOf", LEFT_OF, props)
            checkRule(lp, "rightOf", RIGHT_OF, props)
            checkRule(lp, "alignStart", ALIGN_START, props)
            checkRule(lp, "alignEnd", ALIGN_END, props)
            checkRule(lp, "alignParentStart", ALIGN_PARENT_START, props)
            checkRule(lp, "alignParentEnd", ALIGN_PARENT_END, props)
            checkRule(lp, "startOf", START_OF, props)
            checkRule(lp, "endOf", END_OF, props)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkRule(
        lp: LayoutParams,
        ruleName: String,
        rule: Int,
        props: MutableMap<String, Any?>
    ) {
        val result = lp.getRule(rule)
        if (result != 0) {
            props[ruleName] =
                if (result == TRUE) "TRUE"
                else linkToView(idToString(view.context, result)) {
                    (view.parent as? View)?.findViewById(result)
                }
        }
    }
}