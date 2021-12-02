package com.pitaya.mobile.uinspector.impl.hierarchy

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfoPlugin
import com.pitaya.mobile.uinspector.impl.R
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.pitaya.mobile.uinspector.util.newLine
import com.pitaya.mobile.uinspector.util.withBold
import com.pitaya.mobile.uinspector.util.withColor
import kotlinx.android.synthetic.main.uinspector_panel_hierarchy.view.*

/**
 * @author YvesCheung
 * 2021/1/2
 */
class UInspectorHierarchyPanel(override val priority: Int) : UInspectorChildPanel {

    private val extraInfoService: List<HierarchyExtraInfoPlugin>
        get() = UInspector.plugins[HierarchyExtraInfoPlugin::class.java]

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
                    ssb.withBold {
                        withColor(context) {
                            ssb.newLine(index) {
                                append(view.name)
                                if (!view.id.isNullOrBlank()) {
                                    append("(").append(view.id).append(")")
                                }
                            }
                        }
                    }
                } else {
                    ssb.newLine(index) {
                        append(view.name)
                        if (!view.id.isNullOrBlank()) {
                            append("(").append(view.id).append(")")
                        }
                    }
                }

                extraInfo.forEach {
                    it.afterHierarchy(index, view, ssb)
                }
            }
            root.view_hierarchy.movementMethod = ScrollingMovementMethod()
            root.view_hierarchy.text = ssb
        }
        return root
    }
}