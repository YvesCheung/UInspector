package com.huya.mobile.uinspector.impl.properties

import android.view.View
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
            if (view is TextView) return TextViewPropertiesParser(view)
            return ViewPropertiesParser(view)
        }
    }
}