package com.huya.mobile.uinspector.mask

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.huya.mobile.uinspector.hierarchy.TouchTargets
import com.huya.mobile.uinspector.util.log
import com.huya.mobile.uinspector.util.tryGetActivity

/**
 * @author YvesCheung
 * 2020/12/29
 */
class UInspectorMask @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val gesture = GestureDetector(context,
        object : GestureDetector.SimpleOnGestureListener() {

            private var downEvent: MotionEvent? = null

            private var handle = true

            override fun onDown(e: MotionEvent): Boolean {
                downEvent?.recycle()
                downEvent = MotionEvent.obtain(e)
                return handle
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                val de = downEvent
                val activity = tryGetActivity(context)
                if (de != null && activity != null) {
                    handle = false
                    activity.dispatchTouchEvent(de)

                    val touchTargets = TouchTargets.findFirstTouchTargets(activity.window.decorView)
                    log("dispatch to ${touchTargets.joinToString(" -> ") {
                        it::class.java.simpleName
                    }}")
                    val cancel = MotionEvent.obtain(
                        de.downTime,
                        System.currentTimeMillis(),
                        MotionEvent.ACTION_CANCEL,
                        0f,
                        0f,
                        0
                    )
                    activity.dispatchTouchEvent(cancel)
                    cancel.recycle()
                    handle = true
                    return true
                }
                return false
            }
        })

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return gesture.onTouchEvent(event)
    }
}