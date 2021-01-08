package com.huya.mobile.uinspector.optional.lottie

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParser
import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserService

/**
 * @author YvesCheung
 * 2021/1/8
 */
class LottiePropertiesParserService : ViewPropertiesParserService {

    override fun tryCreate(v: View): ViewPropertiesParser<out View>? {
        if (v is LottieAnimationView) {
            return LottieViewPropertiesParser(v)
        }
        return null
    }
}