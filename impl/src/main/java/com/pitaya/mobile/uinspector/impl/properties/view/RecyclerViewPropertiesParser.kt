package com.pitaya.mobile.uinspector.impl.properties.view

import androidx.recyclerview.widget.RecyclerView
import com.pitaya.mobile.uinspector.util.canonicalName
import com.pitaya.mobile.uinspector.util.simpleName
import com.github.yvescheung.whisper.Output

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
            props["layoutManager"] = lm.simpleName
        }

        val adapter = view.adapter
        if (adapter != null) {
            props["adapter"] = adapter.canonicalName
        }

        if (view.itemDecorationCount > 0) {
            props["itemDecoration"] =
                (0 until view.itemDecorationCount).joinToString { index ->
                    view.getItemDecorationAt(index).canonicalName
                }
        }
    }
}