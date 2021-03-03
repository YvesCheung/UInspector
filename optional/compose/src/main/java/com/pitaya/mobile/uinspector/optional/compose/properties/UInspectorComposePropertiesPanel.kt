package com.pitaya.mobile.uinspector.optional.compose.properties

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.optional.compose.R
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.state.UInspectorTargetViews
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import kotlinx.android.synthetic.main.uinspector_panel_compose_properties.view.*

/**
 * @author YvesCheung
 * 2021/2/2
 */
class UInspectorComposePropertiesPanel(override val priority: Int) : UInspectorChildPanel {

    override val title = "Properties"

    override fun onCreateView(context: Context): View {
        val target =
            UInspector.currentState.withLifecycle?.lastTargetViews?.lastOrNull()
        if (target is ComposeView) {
            val root = LayoutInflater.from(context)
                .inflate(R.layout.uinspector_panel_compose_properties, null)
            root.compose_props_list.adapter = ComposePropsAdapter(target)
            return root
        } else {
            throw AssertionError("Target must be ComposeView!")
        }
    }

    private class ComposePropsAdapter(val target: ComposeView) :
        RecyclerView.Adapter<ViewPropsHolder>(), UInspectorTargetViews.Listener {

        private var props: List<Pair<String, Any?>> = ComposeProperties(target.layoutInfo).toList()

        override fun onChange() {
            val oldProps = props
            props = ComposeProperties(target.layoutInfo).toList()
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun getOldListSize(): Int = oldProps.size

                override fun getNewListSize(): Int = props.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    oldProps[oldItemPosition].first == props[newItemPosition].first

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    oldProps[oldItemPosition].second == props[newItemPosition].second

            }).dispatchUpdatesTo(this)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPropsHolder {
            val textView = TextView(parent.context)
            textView.textSize = 11f
            textView.setTextColor(Color.WHITE)
            textView.isSingleLine = false
            textView.movementMethod = LinkMovementMethod.getInstance()
            return ViewPropsHolder(textView)
        }

        override fun getItemCount(): Int = props.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewPropsHolder, position: Int) {
            val (name, value) = props[position]
            val s = SpannableStringBuilder(name)
                .append(": ")
                .append(if (value is CharSequence) value else value.toString())
            s.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        com.pitaya.mobile.uinspector.impl.R.color.uinspector_primary_color
                    )
                ),
                0,
                name.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            holder.text.text = s
        }
    }

    private class ViewPropsHolder(val text: TextView) : RecyclerView.ViewHolder(text)
}