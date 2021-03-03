package com.pitaya.mobile.uinspector.impl.properties.layoutParam

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.pitaya.mobile.uinspector.impl.UInspectorDefaultPluginService.Companion.PLUGIN_KEY
import com.pitaya.mobile.uinspector.properties.PropertiesParser
import com.pitaya.mobile.uinspector.properties.layoutParam.LayoutParamsPropertiesPlugin

/**
 * @author YvesCheung
 * 2021/3/3
 */
class UInspectorDefaultLayoutParamsPropertiesPlugin : LayoutParamsPropertiesPlugin {

    override fun tryCreate(view: View, lp: ViewGroup.LayoutParams): PropertiesParser {
        return when (lp) {
            is ConstraintLayout.LayoutParams ->
                ConstraintLayoutParamsPropertiesParser(view, lp)
            is LinearLayout.LayoutParams ->
                LinearLayoutParamsPropertiesParser(lp)
            is FrameLayout.LayoutParams ->
                FrameLayoutParamsPropertiesParser(lp)
            is RelativeLayout.LayoutParams ->
                RelativeLayoutParamsPropertiesParser(view, lp)
            else -> LayoutParamsPropertiesParser(lp)
        }
    }

    override val uniqueKey: String = PLUGIN_KEY
}