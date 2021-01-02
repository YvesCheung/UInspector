package com.huya.mobile.uinspector.impl.properties

import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * @author YvesCheung
 * 2020/12/31
 */
class ViewProperties(
    view: View,
    private val actual: LinkedHashMap<String, Any?> = LinkedHashMap()
) : Map<String, Any?> by actual {

    init {
        ViewPropertiesParserFactory.of(view).parse(actual)
    }

    object ViewPropertiesParserFactory {

        fun of(view: View): ViewPropertiesParser<out View> {
            //todo: Add more situation
            return when (view) {
                is ImageView -> ImageViewPropertiesParser(view)
                is TextView -> TextViewPropertiesParser(view)
                else -> ViewPropertiesParser(view)
            }
        }
    }
}