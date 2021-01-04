package com.huya.mobile.uinspector.impl.properties.view

import androidx.recyclerview.widget.RecyclerView

/**
 * @author YvesCheung
 * 2021/1/4
 */
open class RecyclerViewPropertiesParser(view: RecyclerView) : ViewPropertiesParser<RecyclerView>(view) {

    override fun parse(props: MutableMap<String, Any?>) {
        super.parse(props)

        val lm = view.layoutManager
        if (lm != null) {
            props["layoutManager"] = lm::class.java.simpleName
        }

        val adapter = view.adapter
        if (adapter != null) {
            props["adapter"] = adapter::class.java.canonicalName
        }
    }
}