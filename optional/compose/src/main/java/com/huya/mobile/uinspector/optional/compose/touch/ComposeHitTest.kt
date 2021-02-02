package com.huya.mobile.uinspector.optional.compose.touch

import android.view.MotionEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntBounds
import com.huya.mobile.uinspector.hierarchy.AndroidView
import com.huya.mobile.uinspector.hierarchy.HitTest
import com.huya.mobile.uinspector.hierarchy.Layer
import com.huya.mobile.uinspector.optional.compose.hirarchy.AndroidComposeView
import com.huya.mobile.uinspector.optional.compose.hirarchy.ComposeView

/**
 * @author YvesCheung
 * 2021/1/29
 */
class ComposeHitTest(private val delegate: HitTest) : HitTest {

    override fun findNextTarget(event: MotionEvent, current: Layer): Layer? {
        val delegateResult = delegate.findNextTarget(event, current)
        if (delegateResult != null) return delegateResult

        if (current is AndroidComposeView || current is ComposeView) {
            val position = position(event)
            for (child in current.children.toList().asReversed()) {
                if (child is AndroidView) {
                    val childResult = delegate.findNextTarget(event, child)
                    if (childResult != null) {
                        return childResult
                    }
                } else if (child is ComposeView) {
                    val bounds = child.layoutInfo.bounds
                    if (position in bounds) {
                        return child
                    }
                    if (bounds == EMPTY) {
                        val continueFind = findNextTarget(event, child)
                        if (continueFind != null) {
                            return continueFind
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * @see androidx.compose.ui.input.pointer.MotionEventAdapter
     * @see androidx.compose.ui.input.pointer.createPointerInputData
     */
    private fun position(motionEvent: MotionEvent): Offset {
        val pointerCoords = MotionEvent.PointerCoords()
        motionEvent.getPointerCoords(0, pointerCoords)
        return Offset(pointerCoords.x, pointerCoords.y)
    }

    companion object {
        private val EMPTY = IntBounds(0, 0, 0, 0)
    }
}

operator fun IntBounds.contains(position: Offset): Boolean {
    return position.x < this.right && position.x > this.left &&
        position.y < this.bottom && position.y > this.top
}