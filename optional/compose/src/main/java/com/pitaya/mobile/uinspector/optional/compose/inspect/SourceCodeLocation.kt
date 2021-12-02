package com.pitaya.mobile.uinspector.optional.compose.inspect

/**
 * @author YvesCheung
 * 2021/12/2
 */
data class SourceCodeLocation(val file: String, val line: Int, val offset: Int) {

    override fun toString(): String = "$file:$line"
}