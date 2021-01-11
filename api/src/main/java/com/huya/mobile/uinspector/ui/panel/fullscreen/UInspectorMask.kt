package com.huya.mobile.uinspector.ui.panel.fullscreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RestrictTo
import androidx.annotation.Size
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.hierarchy.TouchDispatcher
import com.huya.mobile.uinspector.hierarchy.TouchTargets
import com.huya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.huya.mobile.uinspector.ui.decoration.ViewDecoration
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorPopupPanelContainerImpl
import com.huya.mobile.uinspector.util.findRootParent
import com.huya.mobile.uinspector.util.fromLocation
import com.huya.mobile.uinspector.util.log
import com.huya.mobile.uinspector.util.tryGetActivity

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
                    updateTargetViews(touchTargets)
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
        if (ev.actionMasked == ACTION_DOWN) {
            val activity = tryGetActivity(context)
            if (activity != null) {
                dispatchDecorView =
                    TouchDispatcher.dispatchEventToFindDecorView(activity, ev, currentDecorView)
            }
        } else if (isSingleTap) {
            isSingleTap = false
            TouchDispatcher.dispatchCancelEvent(ev, dispatchDecorView)
        } else {
            TouchDispatcher.dispatchEvent(ev, dispatchDecorView)
        }

        if (ev.actionMasked == ACTION_UP || ev.actionMasked == ACTION_CANCEL) {
            isSingleTap = false
            dispatchDecorView = null
        }
        return consume
    }

    fun updateTargetViews(views: List<View>) {
        val dumpViews =
            views.joinToString(" -> ") { view -> view::class.java.simpleName }
        log("Targets = $dumpViews")

        val state = UInspector.currentState.withLifecycle ?: return

        val oldTarget = state.lastTargetViews?.lastOrNull()
        if (oldTarget != null) {
            decorations.remove(ViewDecoration(oldTarget))
            state.lastTargetViews = null
            popupPanelContainer.dismiss()
        }

        val newTarget = views.lastOrNull()
        if (newTarget != oldTarget && newTarget != null) {
            decorations.add(ViewDecoration(newTarget))
            state.lastTargetViews = views
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

    override fun onDetachedFromWindow() {
        popupPanelContainer.dismiss()
        super.onDetachedFromWindow()
    }
}