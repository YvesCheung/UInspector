package com.huya.mobile.uinspector.impl.properties

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.LinkedHashMap

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

        private val parserFactory =
            ServiceLoader.load(ViewPropertiesParserService::class.java)

        fun of(view: View): ViewPropertiesParser<out View> {
            for (service in parserFactory) {
                val parser = service.tryCreate(view)
                if (parser != null) {
                    return parser
                }
            }
            return when (view) {
                is ImageView -> ImageViewPropertiesParser(view)
                is TextView -> TextViewPropertiesParser(view)
                is RecyclerView -> RecyclerViewPropertiesParser(view)
                is LinearLayout -> LinearLayoutPropertiesParser(view)
                is RelativeLayout -> RelativeLayoutPropertiesParser(view)
                is FrameLayout -> FrameLayoutPropertiesParser(view)
                else -> ViewPropertiesParser(view)
            }
        }
    }
}