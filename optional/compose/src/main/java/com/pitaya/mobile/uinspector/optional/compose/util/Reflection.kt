package com.pitaya.mobile.uinspector.optional.compose.util

import java.lang.reflect.Field

/**
 * @author hautc11
 * 2025/11/20
 */
inline fun <reified T> Any.tryGetField(name: String): T? {
    return try {
        val field = this::class.java.findField(name)
        field.isAccessible = true
        field.get(this) as T?
    } catch (_: Exception) {
        null
    }
}

fun Class<*>.findField(name: String): Field {
    var c: Class<*>? = this
    while (c != null && c != Any::class.java) {
        try {
            return c.getDeclaredField(name)
        } catch (_: NoSuchFieldException) {
            //ignore
        }
        c = c.superclass
    }
    throw NoSuchFieldException("Cannot find field $name in class $this")
}
