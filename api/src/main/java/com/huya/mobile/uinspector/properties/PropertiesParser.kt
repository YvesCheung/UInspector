package com.huya.mobile.uinspector.properties

import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2021/3/3
 */
interface PropertiesParser {

    fun parse(@Output props: MutableMap<String, Any?>)
}