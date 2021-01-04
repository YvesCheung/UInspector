package com.huya.mobile.uinspector.ui.panel.popup

import android.view.Gravity.BOTTOM
import android.view.Gravity.TOP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.huya.mobile.uinspector.R
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.util.log
import com.yy.mobile.whisper.IntDef
import kotlinx.android.synthetic.main.uinspector_popup_panel_container.view.*

/**
 * @author YvesCheung
 * 2020/12/31
 */
internal class UInspectorPopupPanelContainerImpl(val parent: ViewGroup) : UInspectorChildPanelContainer {

    private var popupPanel: UInspectorPopupPanel? = null

    override fun show(anchorView: View) {
        dismiss()
        val childrenPanel = UInspector.createChildPanels()
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
                if (anchorLocation[1] + anchorView.measuredHeight > parent.measuredHeight - panelHeight) {
                    showAt(TOP)
                } else {
                    showAt(BOTTOM)
                }
            }
        }
    }

    override fun dismiss() {
        popupPanel?.dismiss()
        popupPanel = null
    }

    private class UInspectorPopupPanel(
        val inspectorMask: View,
        children: List<UInspectorChildPanel>
    ) {
        private val popupPanel = inspectorMask.popup_panel
        private val viewPager = popupPanel.popup_panel_viewpager
        private val tabLayout = popupPanel.popup_panel_tab

        private val adapter = PanelAdapter(children)

        fun dismiss() {
            if (popupPanel.parent === inspectorMask) {
                log("dismiss UInspectorPopupPanel")
                adapter.createdPanel.keys.forEach {
                    it.onDestroyView()
                }
                (inspectorMask as ViewGroup).removeView(popupPanel)
            }
        }

        fun showAt(@IntDef(TOP, BOTTOM) gravity: Int) {
            log("show UInspectorPopupPanel at ${if (gravity == TOP) "TOP" else "BOTTOM"}")
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
            viewPager.adapter = adapter
            viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    lastSelectedPanelPosition = position
                }
            })
            viewPager.setCurrentItem(lastSelectedPanelPosition, false)
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    private class PanelAdapter(val children: List<UInspectorChildPanel>) : PagerAdapter() {

        val createdPanel = mutableMapOf<UInspectorChildPanel, View>()

        private var currentPrimary: UInspectorChildPanel? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val panel = children[position]
            return createdPanel.getOrPut(panel) {
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

    companion object {

        private var lastSelectedPanelPosition = 0
    }
}