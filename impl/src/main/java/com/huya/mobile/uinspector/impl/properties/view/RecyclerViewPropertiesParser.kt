package com.huya.mobile.uinspector.impl.properties.view

import androidx.recyclerview.widget.RecyclerView
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/4
 */
open class RecyclerViewPropertiesParser(view: RecyclerView) :
    ViewPropertiesParser<RecyclerView>(view) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        val lm = view.layoutManager
        if (lm != null) {
            props["layoutManager"] = lm::class.java.simpleName.ifBlank { lm::class.java.name }
        }

        val adapter = view.adapter
        if (adapter != null) {
            props["adapter"] = adapter::class.java.canonicalName ?: adapter::class.java.name
        }

        if (view.itemDecorationCount > 0) {
            props["itemDecoration"] =
                (0 until view.itemDecorationCount).joinToString { index ->
                    val decoration = view.getItemDecorationAt(index)
                    decoration::class.java.canonicalName ?: decoration::class.java.name
                }
        }
    }
}