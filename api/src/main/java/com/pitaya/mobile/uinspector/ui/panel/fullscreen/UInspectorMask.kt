package com.pitaya.mobile.uinspector.ui.panel.fullscreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RestrictTo
import androidx.annotation.Size
import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.lifecycle.Disposable
import com.pitaya.mobile.uinspector.state.UInspectorTargetViews
import com.pitaya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.pitaya.mobile.uinspector.ui.decoration.ViewDecoration
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorPopupPanelContainerImpl
import com.pitaya.mobile.uinspector.util.findRootParent
import com.pitaya.mobile.uinspector.util.fromLocation
import com.pitaya.mobile.uinspector.util.log
import com.pitaya.mobile.uinspector.util.tryGetActivity
import com.pitaya.mobile.uinspector.hierarchy.*
import com.pitaya.mobile.uinspector.hierarchy.WindowManager

/**
 * @author YvesCheung
 * 2020/12/29
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class UInspectorMask(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * The DecorView that [UInspectorMask] is attached to.
     */
    internal val currentDecorView by lazy(LazyThreadSafetyMode.NONE) { findRootParent() }

    /**
     * [UInspectorMask]'s location
     */
    @get:Size(2)
    private val windowOffset by lazy(LazyThreadSafetyMode.NONE) {
        IntArray(2).also {
            this.getLocationOnScreen(it)
        }
    }

    /**
     * The elements drawn on our [UInspectorMask]
     */
    private val decorations: MutableList<UInspectorDecoration> = mutableListOf()

    internal val popupPanelContainer = UInspectorPopupPanelContainerImpl(this)

    private val gesture = GestureDetector(context,
        object : GestureDetector.SimpleOnGestureListener() {

            private var downEvent: MotionEvent? = null

            override fun onDown(e: MotionEvent): Boolean {
                downEvent?.recycle()
                downEvent = obtain(e)
                return true
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                isSingleTap = true
                return super.onSingleTapUp(e)
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                val event = downEvent
                val activity = tryGetActivity(context)
                if (event != null && activity != null) {
                    val touchTargets =
                        event.fromLocation(windowOffset) {
                            TouchTargets.findTouchTargets(activity, event, currentDecorView)
                        }
                    updateTargetLayers(touchTargets)
                    return true
                }
                return false
            }
        })

    private var isSingleTap = false

    private var dispatchDecorView: View? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val consume = gesture.onTouchEvent(ev)

        ev.fromLocation(windowOffset) {
            if (ev.actionMasked == ACTION_DOWN) {
                val activity = tryGetActivity(context)
                if (activity != null) {
                    //find the real target who can consume this event
                    dispatchDecorView =
                        TouchDispatcher.dispatchEventToFindDecorView(activity, ev, currentDecorView)
                }
            } else if (isSingleTap) { //single tap is consumed by us. dispatch cancel to target.
                isSingleTap = false
                TouchDispatcher.dispatchCancelEvent(ev, dispatchDecorView)
            } else {
                TouchDispatcher.dispatchEvent(ev, dispatchDecorView)
            }

            if (ev.actionMasked == ACTION_UP || ev.actionMasked == ACTION_CANCEL) {
                isSingleTap = false
                dispatchDecorView = null
            }
        }
        return consume
    }

    override fun dispatchKeyEvent(keyEvent: KeyEvent?): Boolean {
        val activity = tryGetActivity(context)
        var handle = false
        if (activity != null) {
            val decorViews = WindowManager.findDecorViews(activity)
            for (decor in decorViews.asReversed()) {
                if (currentDecorView === decor) { //exclude current decorView
                    continue
                }

                handle = decor.dispatchKeyEvent(keyEvent)
                break
            }
        }
        return handle
    }

    fun updateTargetViews(views: List<View>) {
        updateTargetLayers(views.map(LayerFactoryPlugin::create))
    }

    fun updateTargetLayers(layers: List<Layer>) {
        val dumpViews =
            layers.joinToString(" -> ") { layer -> layer.name }
        log("Targets = $dumpViews")

        val state = UInspector.currentState.withLifecycle ?: return

        val oldTarget = state.lastTargetViews?.lastOrNull()
        if (oldTarget != null) {
            decorations.remove(ViewDecoration(oldTarget))
            state.lastTargetViews?.clear()
            state.lastTargetViews = null
            popupPanelContainer.dismiss()
        }

        val newTarget = layers.lastOrNull()
        if (newTarget != oldTarget && newTarget != null) {
            val decoration = ViewDecoration(newTarget)
            decorations.add(decoration)
            state.lastTargetViews = UInspectorTargetViews(layers)
                .addOnScrollListener(object : UInspectorTargetViews.Listener {
                    override fun onChange() {
                        invalidate()
                    }
                })
                .addOnDrawListener(object : UInspectorTargetViews.Listener {
                    override fun onChange() {
                        invalidate()
                    }
                })
                .addOnDetachListener(object : UInspectorTargetViews.Listener {
                    override fun onChange() {
                        decorations.remove(decoration)
                        popupPanelContainer.dismiss()
                    }
                })
            popupPanelContainer.show(newTarget)
        }
        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val c = canvas.save()
        canvas.translate(-windowOffset[0].toFloat(), -windowOffset[1].toFloat())
        try {
            decorations.forEach { it.draw(canvas) }
        } finally {
            canvas.restoreToCount(c)
        }
        super.dispatchDraw(canvas)
    }

    fun addDecoration(decoration: UInspectorDecoration): Disposable {
        decorations.add(decoration)
        return Disposable.create {
            removeDecoration(decoration)
        }
    }

    fun removeDecoration(decoration: UInspectorDecoration) {
        decorations.remove(decoration)
    }

    override fun onDetachedFromWindow() {
        popupPanelContainer.dismiss()
        super.onDetachedFromWindow()
    }
}