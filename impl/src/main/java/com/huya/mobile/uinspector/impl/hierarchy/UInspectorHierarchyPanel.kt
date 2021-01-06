package com.huya.mobile.uinspector.impl.hierarchy

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.impl.R
import com.huya.mobile.uinspector.impl.hierarchy.extra.DefaultHierarchyExtraInfoService
import com.huya.mobile.uinspector.impl.hierarchy.extra.HierarchyExtraInfoService
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.huya.mobile.uinspector.util.idToString
import kotlinx.android.synthetic.main.uinspector_panel_hierarchy.view.*
import java.util.*

/**
 * @author YvesCheung
 * 2021/1/2
 */
class UInspectorHierarchyPanel(override val priority: Int) : UInspectorChildPanel {

    override val title = "Hierarchy"

    @SuppressLint("InflateParams")
    override fun onCreateView(context: Context): View {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.uinspector_panel_hierarchy, null)
        val lifecycleState = UInspector.currentState.withLifecycle
        val hierarchy = lifecycleState?.lastTargetViews
        val activity = lifecycleState?.activity
        val targetView = hierarchy?.lastOrNull()
        if (activity != null && targetView != null) {

            val extraInfo =
                extraInfoService.flatMap { it.create(activity, targetView) }

            val ssb = SpannableStringBuilder()

            for ((index, view) in hierarchy.withIndex()) {
                extraInfo.forEach {
                    it.beforeHierarchy(index, view, ssb)
                }

                if (index == hierarchy.lastIndex) {
                    ssb.withColor(context) {
                        ssb.newLine(index) {
                            append(view::class.java.simpleName)
                            if (view.id > 0) {
                                append("(${idToString(view.context, view.id)})")
                            }
                        }
                    }
                } else {
                    ssb.newLine(index) {
                        append(view::class.java.simpleName)
                        if (view.id > 0) {
                            append("(${idToString(view.context, view.id)})")
                        }
                    }
                }

                extraInfo.forEach {
                    it.afterHierarchy(index, view, ssb)
                }
            }
            root.view_hierarchy.text = ssb
        }
        return root
    }

    companion object {

        private val extraInfoService: List<HierarchyExtraInfoService> by lazy(LazyThreadSafetyMode.NONE) {
            val services =
                ServiceLoader.load(HierarchyExtraInfoService::class.java).toMutableList()
            services.add(DefaultHierarchyExtraInfoService())
            services
        }

        inline fun SpannableStringBuilder.newLine(
            indentSize: Int,
            write: SpannableStringBuilder.() -> Unit
        ) {
            var size = indentSize
            while (size-- > 0) {
                append(" ")
            }
            append("- ")
            write()
            append("\n")
        }

        inline fun SpannableStringBuilder.withColor(
            context: Context,
            @ColorRes color: Int = R.color.uinspector_primary_color,
            write: SpannableStringBuilder.() -> Unit
        ) {
            val start = length
            write()
            val end = length
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, color)),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}