package com.pitaya.mobile.uinspector.optional.lottie

import android.util.Log
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable.INFINITE
import com.airbnb.lottie.LottieDrawable.RESTART
import com.pitaya.mobile.uinspector.impl.properties.view.ImageViewPropertiesParser
import com.pitaya.mobile.uinspector.util.idToString
import com.pitaya.mobile.uinspector.util.quote
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/8
 */
open class LottieViewPropertiesParser(private val animView: LottieAnimationView) :
    ImageViewPropertiesParser(animView) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        val fileName = getAnimationName(animView)
        if (!fileName.isNullOrBlank()) {
            props["lottie fileName"] = fileName.quote()
        }

        val id = getAnimationId(animView)
        if (id > 0) {
            props["lottie rawRes"] = idToString(animView.context, id)
        }

        val duration = animView.duration
        if (duration > 0L) {
            props["lottie duration"] = duration
        }

        val speed = animView.speed
        if (speed != 1f) {
            props["lottie speed"] = speed
        }

        props["lottie repeat mode"] =
            if (animView.repeatMode == RESTART) "RESTART"
            else "REVERSE"

        props["lottie repeat count"] =
            if (animView.repeatCount == INFINITE) "INFINITE"
            else animView.repeatCount
    }

    companion object {

        private var noAnimationName = false

        private fun getAnimationName(view: LottieAnimationView): String? {
            if (!noAnimationName) {
                try {
                    return animationName.get(view) as String?
                } catch (e: NoSuchFieldException) {
                    noAnimationName = true
                    Log.e("UInspector", e.toString())
                } catch (e: Throwable) {
                    Log.e("UInspector", e.toString())
                }
            }
            return null
        }

        private val animationName by lazy(LazyThreadSafetyMode.NONE) {
            val f = LottieAnimationView::class.java.getDeclaredField("animationName")
            f.isAccessible = true
            f
        }

        private var noAnimationRes = false

        private fun getAnimationId(view: LottieAnimationView): Int {
            if (!noAnimationRes) {
                try {
                    return animationResId.get(view) as Int
                } catch (e: NoSuchFieldException) {
                    noAnimationRes = true
                    Log.e("UInspector", e.toString())
                } catch (e: Throwable) {
                    Log.e("UInspector", e.toString())
                }
            }
            return 0
        }

        private val animationResId by lazy(LazyThreadSafetyMode.NONE) {
            val f = LottieAnimationView::class.java.getDeclaredField("animationResId")
            f.isAccessible = true
            f
        }
    }
}