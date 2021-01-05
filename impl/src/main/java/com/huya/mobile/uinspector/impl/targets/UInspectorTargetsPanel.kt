package com.huya.mobile.uinspector.impl.targets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.impl.R
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.huya.mobile.uinspector.util.idToString
import com.huya.mobile.uinspector.util.visibilityToString
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
                root.view_targets_parent_title.visibility = VISIBLE
                root.view_targets_parent.visibility = VISIBLE
                root.view_targets_parent.adapter = Adapter(listOf(IndexedValue<View>(-1, parent)))
            } else {
                root.view_targets_parent_title.visibility = GONE
                root.view_targets_parent.visibility = GONE
            }

            val children =
                if (targetView is ViewGroup) targetView.children.withIndex().toList()
                else emptyList()
            if (children.isNotEmpty()) {
                root.view_targets_children_title.visibility = VISIBLE
                root.view_targets_children.visibility = VISIBLE
                root.view_targets_children.adapter = Adapter(children)
            } else {
                root.view_targets_children_title.visibility = GONE
                root.view_targets_children.visibility = GONE
            }

            val brothers =
                if (parent is ViewGroup)
                    parent.children.withIndex()
                        .filterTo(mutableListOf()) { it.value !== targetView }
                else
                    @Suppress("RemoveExplicitTypeArguments") emptyList<IndexedValue<View>>()
            if (brothers.isNotEmpty()) {
                root.view_targets_brother_title.visibility = VISIBLE
                root.view_targets_brother.visibility = VISIBLE
                root.view_targets_brother.adapter = Adapter(brothers)
            } else {
                root.view_targets_brother_title.visibility = GONE
                root.view_targets_brother.visibility = GONE
            }
        }
        return root
    }

    private inner class Adapter(val views: List<IndexedValue<View>>) : RecyclerView.Adapter<VH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.uinspector_view_list_item, parent, false)
            return VH(root.uinspector_view_access_btn)
        }

        override fun getItemCount(): Int = views.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val ctx = holder.itemView.context
            val (index, target) = views[position]
            val name = StringBuilder(target::class.java.simpleName)
            if (target.id > 0) {
                name.append("(${idToString(ctx, target.id)})")
            }
            if (index >= 0) {
                name.append("(index $index)")
            }
            if (target.visibility != VISIBLE) {
                holder.textView.setTextColor(ctx.resources.getColor(android.R.color.darker_gray))
                name.append("(${visibilityToString(target.visibility)})")
            }
            holder.textView.gravity = Gravity.CENTER_VERTICAL or Gravity.START
            holder.textView.text = name
            holder.textView.setOnClickListener {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || target.isAttachedToWindow) {
                    UInspector.currentState.withLifecycle?.panel?.updateTargetView(target)
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