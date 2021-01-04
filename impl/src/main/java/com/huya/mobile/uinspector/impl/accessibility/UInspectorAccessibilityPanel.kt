package com.huya.mobile.uinspector.impl.accessibility

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.impl.R
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import kotlinx.android.synthetic.main.uinspector_view_list_item.view.*

/**
 * @author YvesCheung
 * 2021/1/3
 */
class UInspectorAccessibilityPanel(override val priority: Int) : UInspectorChildPanel {

    override val title: CharSequence = "Accessibility"

    //todo: support more action...
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private val actions = listOf(
        AccessibilityAction("ACTION_CLICK", ACTION_CLICK),
        AccessibilityAction("ACTION_LONG_CLICK", ACTION_LONG_CLICK),
        AccessibilityAction("ACTION_FOCUS", ACTION_FOCUS),
        AccessibilityAction("ACTION_CLEAR_FOCUS", ACTION_CLEAR_FOCUS),
        AccessibilityAction("ACTION_SELECT", ACTION_SELECT),
        AccessibilityAction("ACTION_CLEAR_SELECTION", ACTION_CLEAR_SELECTION)
    )

    @SuppressLint("SetTextI18n")
    override fun onCreateView(context: Context): View {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val recyclerView = RecyclerView(context)
            val targetView =
                UInspector.currentState.withLifecycle?.lastTargetViews?.lastOrNull()
            if (targetView != null) {
                recyclerView.layoutManager = LinearLayoutManager(context, VERTICAL, false)
                recyclerView.adapter = Adapter(targetView)
            }
            return recyclerView
        }
        val unSupportText = TextView(context)
        unSupportText.text =
            "SDK version must not less than JELLY_BEAN(16) but current is ${Build.VERSION.SDK_INT}"
        return unSupportText
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private inner class Adapter(val targetView: View) : RecyclerView.Adapter<VH>() {
        @SuppressLint("InflateParams")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.uinspector_view_list_item, parent, false)
            return VH(root.uinspector_view_access_btn)
        }

        override fun getItemCount(): Int = actions.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val action = actions[position]
            holder.textView.text = action.name
            holder.textView.setOnClickListener {
                try {
                    targetView.performAccessibilityAction(action.action, action.arguments)
                } catch (e: Throwable) {
                    Log.e("UInspectorAccessibility", e.toString())
                }
            }
        }
    }

    private class VH(val textView: TextView) : RecyclerView.ViewHolder(textView)

    private data class AccessibilityAction(
        val name: String,
        val action: Int,
        val arguments: Bundle? = null
    )
}