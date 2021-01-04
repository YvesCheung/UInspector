package com.huya.mobile.uinspector.impl.targets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.impl.R
import com.huya.mobile.uinspector.impl.utils.idToString
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import kotlinx.android.synthetic.main.uinspector_panel_targets.view.*
import kotlinx.android.synthetic.main.uinspector_view_list_item.view.*

/**
 * @author YvesCheung
 * 2021/1/4
 */
class UInspectorTargetsPanel(override val priority: Int) : UInspectorChildPanel {

    override val title = "Targets"

    @SuppressLint("InflateParams")
    override fun onCreateView(context: Context): View {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.uinspector_panel_targets, null)
        val targetView =
            UInspector.currentState.withLifecycle?.lastTargetViews?.lastOrNull()
        if (targetView != null) {
            val parent = targetView.parent
            if (parent is View) {
                root.view_targets_parent_title.visibility = View.VISIBLE
                root.view_targets_parent.visibility = View.VISIBLE
                root.view_targets_parent.adapter = Adapter(listOf(parent))
            } else {
                root.view_targets_parent_title.visibility = View.GONE
                root.view_targets_parent.visibility = View.GONE
            }

            val children =
                if (targetView is ViewGroup) targetView.children.toList()
                else emptyList()
            if (children.isNotEmpty()) {
                root.view_targets_children_title.visibility = View.VISIBLE
                root.view_targets_children.visibility = View.VISIBLE
                root.view_targets_children.adapter = Adapter(children)
            } else {
                root.view_targets_children_title.visibility = View.GONE
                root.view_targets_children.visibility = View.GONE
            }

            val brothers =
                if (parent is ViewGroup) parent.children.filter { it !== targetView }.toList()
                else emptyList()
            if (brothers.isNotEmpty()) {
                root.view_targets_brother_title.visibility = View.VISIBLE
                root.view_targets_brother.visibility = View.VISIBLE
                root.view_targets_brother.adapter = Adapter(brothers)
            } else {
                root.view_targets_brother_title.visibility = View.GONE
                root.view_targets_brother.visibility = View.GONE
            }
        }
        return root
    }

    private inner class Adapter(val views: List<View>) : RecyclerView.Adapter<VH>() {
        @SuppressLint("InflateParams")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.uinspector_view_list_item, parent, false)
            return VH(root.uinspector_view_access_btn)
        }

        override fun getItemCount(): Int = views.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val v = views[position]
            var name = v::class.java.simpleName
            if (v.id > 0) {
                name += "(${idToString(holder.itemView.context, v.id)})"
            }
            holder.textView.text = name
            holder.textView.setOnClickListener {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || v.isAttachedToWindow) {
                    UInspector.currentState.withLifecycle?.panel?.updateTargetView(v)
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "View has been detached!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private class VH(val textView: TextView) : RecyclerView.ViewHolder(textView)
}