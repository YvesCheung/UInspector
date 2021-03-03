package com.huya.mobile.uinspector.optional.lottie

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParser
import com.huya.mobile.uinspector.properties.view.ViewPropertiesPlugin

/**
 * @author YvesCheung
 * 2021/1/8
 */
open class LottiePropertiesParserPlugin : ViewPropertiesPlugin {

    override val uniqueKey: String = "Lottie"

    override fun tryCreate(view: View): ViewPropertiesParser<out View>? {
        if (view is LottieAnimationView) {
            return LottieViewPropertiesParser(view)
        }
        return null
    }
}