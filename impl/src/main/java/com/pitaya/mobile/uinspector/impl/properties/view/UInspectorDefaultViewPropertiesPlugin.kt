package com.pitaya.mobile.uinspector.impl.properties.view

import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pitaya.mobile.uinspector.impl.UInspectorDefaultPluginService.Companion.PLUGIN_KEY
import com.pitaya.mobile.uinspector.properties.PropertiesParser
import com.pitaya.mobile.uinspector.properties.view.ViewPropertiesPlugin

/**
 * The default implementation for [ViewPropertiesPlugin]
 *
 * @author YvesCheung
 * 2021/3/3
 */
class UInspectorDefaultViewPropertiesPlugin : ViewPropertiesPlugin {

    override fun tryCreate(view: View): PropertiesParser {
        return when (view) {
            is ImageView -> ImageViewPropertiesParser(view)
            is TextView -> TextViewPropertiesParser(view)
            is RecyclerView -> RecyclerViewPropertiesParser(view)
            is LinearLayout -> LinearLayoutPropertiesParser(view)
            is RelativeLayout -> RelativeLayoutPropertiesParser(view)
            is FrameLayout -> FrameLayoutPropertiesParser(view)
            is ViewGroup -> ViewGroupPropertiesParser(view)
            else -> ViewPropertiesParser(view)
        }
    }

    override val uniqueKey: String = PLUGIN_KEY
}