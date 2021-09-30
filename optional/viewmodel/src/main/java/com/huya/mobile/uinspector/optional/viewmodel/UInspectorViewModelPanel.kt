package com.huya.mobile.uinspector.optional.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreAccessor
import androidx.lifecycle.ViewModelStoreOwner
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.hierarchy.FragmentsFinder
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.huya.mobile.uinspector.util.canonicalName
import com.huya.mobile.uinspector.util.newLine
import com.huya.mobile.uinspector.util.withBold
import com.huya.mobile.uinspector.util.withColor
import kotlinx.android.synthetic.main.uinspector_panel_viewmodel.view.*

/**
 * @author YvesCheung
 * 2021/9/30
 */
class UInspectorViewModelPanel(override val priority: Int) : UInspectorChildPanel {

    override val title: CharSequence = "ViewModel"

    @SuppressLint("InflateParams")
    override fun onCreateView(context: Context): View {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.uinspector_panel_viewmodel, null)
        val lifecycleState = UInspector.currentState.withLifecycle
        val hierarchy = lifecycleState?.lastTargetViews
        val activity = lifecycleState?.activity
        val targetView = hierarchy?.lastOrNull()
        if (activity != null && targetView != null) {
            val ssb = SpannableStringBuilder()
            var lineNum = 0
            var foundViewModel = false

            ssb.withBold {
                newLine(lineNum) {
                    append(activity.canonicalName)
                }
            }
            val activityViewModels = tryGetViewModels(activity)
            if (activityViewModels.isNotEmpty()) {
                for (vm in activityViewModels) {
                    ssb.withColor(context) {
                        newLine(lineNum) {
                            append(vm.canonicalName)
                        }
                    }
                }
                foundViewModel = true
                lineNum++
            }

            val fragments = FragmentsFinder.findFragments(activity)
            for (view in hierarchy) {
                val fragment = fragments[view] ?: continue
                val viewModels = tryGetViewModels(fragment)
                if (viewModels.isNotEmpty()) {
                    ssb.withBold {
                        newLine(lineNum) {
                            append(fragment.canonicalName)
                        }
                    }
                    for (vm in viewModels) {
                        ssb.withColor(context) {
                            newLine(lineNum) {
                                append(vm.canonicalName)
                            }
                        }
                    }
                    foundViewModel = true
                    lineNum++
                }
            }

            if (!foundViewModel) {
                ssb.withColor(context, R.color.uinspector_error_color) {
                    newLine(lineNum) {
                        append("Can not found any ViewModel!")
                    }
                }
            }
            root.view_viewmodel.text = ssb
        }
        return root
    }

    private fun tryGetViewModels(activityOrFragment: Any): List<ViewModel> {
        if (activityOrFragment is ViewModelStoreOwner) {
            val store = activityOrFragment.viewModelStore
            return ViewModelStoreAccessor.keys(store).map { key ->
                ViewModelStoreAccessor.get(store, key)
            }
        }
        return emptyList()
    }
}