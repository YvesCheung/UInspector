package com.pitaya.mobile.uinspector.optional.compose.touch

import android.view.MotionEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.inspector.InspectorNode
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.HitTest
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.AndroidComposeView
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView

/**
 * @author YvesCheung
 * 2021/1/29
 */
class ComposeHitTest(private val delegate: HitTest) : HitTest {

    override fun findNextTarget(event: MotionEvent, current: Layer): Layer? {
        val delegateResult = delegate.findNextTarget(event, current)
        if (delegateResult != null) return delegateResult

        if (current is AndroidComposeView || current is ComposeView) {
            //todo: Why needs transform into Offset in compose?
            //val position = position(event)
            for (child in current.children.toList().asReversed()) {
                if (child is AndroidView) {
                    val childResult = delegate.findNextTarget(event, child)
                    if (childResult != null) {
                        return childResult
                    }
                } else if (child is ComposeView) {
                    val node = child.layoutInfo
                    if (event in node) {
                        return child
                    }
//                    if (node == EMPTY) {
//                        val continueFind = findNextTarget(event, child)
//                        if (continueFind != null) {
//                            return continueFind
//                        }
//                    }
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
}

operator fun InspectorNode.contains(position: MotionEvent): Boolean {
    return position.x < this.left + this.width && position.x > this.left &&
        position.y < this.top - this.height && position.y > this.top
}

//operator fun IntBounds.contains(position: MotionEvent): Boolean {
//    return position.x < this.right && position.x > this.left &&
//        position.y < this.bottom && position.y > this.top
//}