package com.pitaya.mobile.uinspector.properties

import com.pitaya.mobile.uinspector.util.Output

/**
 * @author YvesCheung
 * 2021/3/3
 */
interface PropertiesParser {

    fun parse(@Output props: MutableMap<String, Any?>)
}