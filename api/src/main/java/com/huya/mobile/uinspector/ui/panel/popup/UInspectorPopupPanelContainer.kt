package com.huya.mobile.uinspector.ui.panel.popup

import android.view.Gravity.BOTTOM
import android.view.Gravity.TOP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.huya.mobile.uinspector.R
import com.huya.mobile.uinspector.UInspector
import com.yy.mobile.whisper.IntDef
import kotlinx.android.synthetic.main.uinspector_popup_panel_container.view.*

/**
 * @author YvesCheung
 * 2020/12/31
 */
internal class UInspectorPopupPanelContainer {

    private var popupPanel: UInspectorPopupPanel? = null

    fun show(anchorView: View, parent: ViewGroup) {
        dismiss()
        val childrenPanel = UInspector.childPanels.toList()
        if (childrenPanel.isNotEmpty()) {
            popupPanel = UInspectorPopupPanel(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.uinspector_popup_panel_container, parent),
                childrenPanel
            ).apply {
                val anchorLocation =
                    IntArray(2).also { anchorView.getLocationOnScreen(it) }
                val panelHeight = anchorView.context.resources
                    .getDimension(R.dimen.popup_panel_container_height)
                //Note: parent.measuredHeight is close to the screen height.
                //  It's not 100% accurate
                if (anchorLocation[1] > parent.measuredHeight - panelHeight) {
                    showAt(TOP)
                } else {
                    showAt(BOTTOM)
                }
            }
        }
    }

    fun dismiss() {
        popupPanel?.dismiss()
        popupPanel = null
    }

    private class UInspectorPopupPanel(
        val inspectorMask: View,
        val children: List<UInspectorChildPanel>
    ) {

        private val popupPanel = inspectorMask.popup_panel
        private val viewPager = popupPanel.popup_panel_viewpager
        private val tabLayout = popupPanel.popup_panel_tab

        fun dismiss() {
            if (popupPanel.parent === inspectorMask) {
                (inspectorMask as ViewGroup).removeView(popupPanel)
            }
        }

        fun showAt(@IntDef(TOP, BOTTOM) gravity: Int) {
            initView()

            val lp = popupPanel.layoutParams
            if (lp is FrameLayout.LayoutParams) {
                lp.gravity = gravity
            } else if (lp is LinearLayout.LayoutParams) {
                lp.gravity = gravity
            }
            popupPanel.layoutParams = lp
        }

        private fun initView() {
            viewPager.adapter = PanelAdapter(children)
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    private class PanelAdapter(val children: List<UInspectorChildPanel>) : PagerAdapter() {

        private var currentPrimary: UInspectorChildPanel? = null

        private val cacheView = mutableMapOf<UInspectorChildPanel, View>()

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val panel = children[position]
            return cacheView.getOrPut(panel) {
                val child = panel.onCreateView(container.context)
                container.addView(child)
                child
            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
            val nextPrimary = children[position]
            if (currentPrimary != nextPrimary) {
                currentPrimary?.onUserVisibleHint(false)
                nextPrimary.onUserVisibleHint(true)
            }
            currentPrimary = nextPrimary
        }

        override fun getPageTitle(position: Int) = children[position].title

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun getCount(): Int = children.size
    }
}