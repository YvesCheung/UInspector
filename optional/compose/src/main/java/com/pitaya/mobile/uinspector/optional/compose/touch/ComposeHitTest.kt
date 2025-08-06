package com.pitaya.mobile.uinspector.optional.compose.touch

import android.view.MotionEvent
import androidx.compose.ui.unit.IntOffset
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.HitTest
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.AndroidComposeView
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.SubComposition
import kotlin.math.roundToInt

/**
 * @author YvesCheung
 * 2021/1/29
 */
class ComposeHitTest(private val delegate: HitTest) : HitTest {

    override fun findNextTarget(event: MotionEvent, current: Layer): Layer? {
        val delegateResult = delegate.findNextTarget(event, current)
        if (delegateResult != null) return delegateResult

        if (current is AndroidComposeView || current is ComposeView || current is SubComposition) {
            return findNextComposeTarget(event, current)
        }
        /**
         * current may be [androidx.compose.ui.platform.AndroidViewsHandler]
         */
        val parent = current.parent
        if (current is AndroidView && parent is AndroidComposeView) {
            return findNextComposeTarget(event, parent)
        }
        return null
    }

    private fun findNextComposeTarget(event: MotionEvent, current: Layer): Layer? {
        for (child in current.children.toList().asReversed()) {
            if (child is AndroidView) {
                val childResult = delegate.findNextTarget(event, child)
                if (childResult != null) {
                    return childResult
                }
            } else if (child is ComposeView) {
                if (event.isOnView(child)) {
                    return child
                }
            } else if (child is SubComposition) {
                val continueFind = findNextTarget(event, child)
                if (continueFind != null) {
                    return child
                }
            }
        }
        return null
    }

    private fun MotionEvent.isOnView(view: ComposeView): Boolean {
        val position = IntOffset(x.roundToInt(), y.roundToInt())
        return view.bounds.contains(position)
    }
}

