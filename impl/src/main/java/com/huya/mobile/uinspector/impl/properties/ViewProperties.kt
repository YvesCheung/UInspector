package com.huya.mobile.uinspector.impl.properties

import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.huya.mobile.uinspector.impl.properties.layoutParam.*
import com.huya.mobile.uinspector.impl.properties.view.*
import com.huya.mobile.uinspector.util.loadService

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
        val lp = view.layoutParams
        if (lp != null) {
            LayoutParamsPropertiesParserFactory.of(view, lp).parse(actual)
        }
    }

    object ViewPropertiesParserFactory {

        private val parserFactory =
            loadService(ViewPropertiesParserService::class.java)

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
                is ViewGroup -> ViewGroupPropertiesParser(view)
                else -> ViewPropertiesParser(view)
            }
        }
    }

    object LayoutParamsPropertiesParserFactory {

        private val parserFactory =
            loadService(LayoutParamsPropertiesParserService::class.java)

        fun of(view: View, lp: ViewGroup.LayoutParams):
            LayoutParamsPropertiesParser<out ViewGroup.LayoutParams> {
            for (service in parserFactory) {
                val parser = service.tryCreate(view, lp)
                if (parser != null) {
                    return parser
                }
            }
            return when (lp) {
                is ConstraintLayout.LayoutParams ->
                    ConstraintLayoutParamsPropertiesParser(view, lp)
                is LinearLayout.LayoutParams ->
                    LinearLayoutParamsPropertiesParser(lp)
                is FrameLayout.LayoutParams ->
                    FrameLayoutParamsPropertiesParser(lp)
                is RelativeLayout.LayoutParams ->
                    RelativeLayoutParamsPropertiesParser(view, lp)
                is CoordinatorLayout.LayoutParams ->
                    CoordinatorLayoutParamsPropertiesParser(view, lp)
                is AppBarLayout.LayoutParams ->
                    AppBarLayoutParamsPropertiesParser(view, lp)
                else -> LayoutParamsPropertiesParser(lp)
            }
        }
    }
}