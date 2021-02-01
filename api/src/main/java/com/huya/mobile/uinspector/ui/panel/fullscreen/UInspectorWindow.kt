package com.huya.mobile.uinspector.ui.panel.fullscreen

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat.TRANSPARENT
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import com.huya.mobile.uinspector.hierarchy.Layer
import com.huya.mobile.uinspector.lifecycle.Disposable
import com.huya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelContainer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author YvesCheung
 * 2021/1/7
 */
class UInspectorWindow : UInspectorPanel {

    private var windowManager: WindowManager? = null

    private val delegate = UInspectorPanelDelegate()

    private var added = AtomicBoolean(false)

    override val childPanelContainer: UInspectorChildPanelContainer?
        get() = delegate.childPanelContainer

    override fun show(activity: Activity) {
        windowManager =
            activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (added.compareAndSet(false, true)) {
            windowManager?.addView(
                delegate.onCreateView(activity),
                WindowManager.LayoutParams(
                    TYPE_APPLICATION_OVERLAY,
                    FLAG_LAYOUT_IN_SCREEN,
                    TRANSPARENT
                )
            )
        }
    }

    override fun close() {
        if (added.compareAndSet(true, false)) {
            val view = delegate.mask
            if (view != null) windowManager?.removeView(view)
        }
    }

    override fun updateTargetViews(views: List<View>) = delegate.updateTargetViews(views)

    override fun updateTargetView(view: View) = delegate.updateTargetView(view)

    override fun updateTargetLayer(layer: Layer) = delegate.updateTargetLayer(layer)

    override fun updateTargetLayers(layers: List<Layer>) = delegate.updateTargetLayers(layers)

    override fun addDecoration(decoration: UInspectorDecoration): Disposable =
        delegate.addDecoration(decoration)

    override fun removeDecoration(decoration: UInspectorDecoration) =
        delegate.removeDecoration(decoration)

    override fun invalidate() = delegate.invalidateMask()
}