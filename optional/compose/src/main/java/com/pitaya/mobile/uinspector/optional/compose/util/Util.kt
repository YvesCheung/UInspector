package com.pitaya.mobile.uinspector.optional.compose.util

import com.pitaya.mobile.uinspector.util.canonicalName

val Any.simpleName: String
    get() {
        var name = this.canonicalName
        if (name.contains(".")) {
            name = name.substring(name.lastIndexOf('.') + 1)
        }
        return name
    }
