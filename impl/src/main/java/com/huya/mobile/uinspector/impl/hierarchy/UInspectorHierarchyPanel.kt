package com.huya.mobile.uinspector.impl.hierarchy

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.impl.R
import com.huya.mobile.uinspector.impl.utils.idToString
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import kotlinx.android.synthetic.main.uinspector_panel_hierarchy.view.*

/**
 * @author YvesCheung
 * 2021/1/2
 */
class UInspectorHierarchyPanel(override val priority: Int) : UInspectorChildPanel {

    override val title = "hierarchy"

    @SuppressLint("InflateParams")
    override fun onCreateView(context: Context): View {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.uinspector_panel_hierarchy, null)
        val hierarchy =
            UInspector.currentState.withLifecycle?.lastTouchTargets
        if (hierarchy != null) {
            val str = StringBuilder()
            for ((index, view) in hierarchy.withIndex()) {
                var indent = index
                while (indent-- > 0) {
                    str.append(" ")
                }
                str.append("-").append(viewToString(view)).append("\n")
            }
            root.view_hierarchy.text = str.toString()
        }
        return root
    }

    private fun viewToString(view: View): String {
        var str = view::class.java.simpleName
        if (view.id > 0) {
            str += "(${idToString(view.context, view.id)})"
        }
        return str
    }
}