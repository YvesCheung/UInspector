package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier
import com.pitaya.mobile.uinspector.optional.compose.util.tryGetField
import com.pitaya.mobile.uinspector.util.canonicalName

/**
 * @author hautc11
 * 2025/11/20
 */
class TextStringSimpleElementParser(private val modifier: Modifier) : ComposePropertiesParser {

    override val priority: Int = 10

    override fun parse(props: MutableMap<String, Any?>) {
        modifier.tryGetField<Int>("minLines")?.let { props["minLines"] = it }
        modifier.tryGetField<Int>("maxLines")?.let {
            props["maxLines"] = if (it != Int.MAX_VALUE) {
                it
            } else {
                "No limited."
            }
        }
        modifier.tryGetField<Any>("overflow")?.let { props["overflow"] = it.toString() }
        modifier.tryGetField<Boolean>("softWrap")?.let { props["softWrap"] = it }
    }

    companion object {

        fun accept(modifier: Modifier): Boolean {
            return modifier.canonicalName.endsWith("TextStringSimpleElement")
        }
    }
}