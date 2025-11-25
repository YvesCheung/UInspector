package com.pitaya.mobile.uinspector.optional.compose.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun colorToString(color: Any): String {
    val finalColor = when (color) {
        is Color -> color
        is Long -> Color(color.toULong())
        else -> null
    }

    if (finalColor != null) {
        return "0x" + Integer.toHexString(finalColor.toArgb()).uppercase()
    }
    return color.toString()
}
