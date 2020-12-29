package com.huya.mobile.uinspector.lifecycle

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.annotation.RestrictTo
import com.huya.mobile.uinspector.UInspector

/**
 * @author YvesCheung
 * 2020/12/28
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class UInspectorInstaller : ContentProvider() {

    override fun onCreate(): Boolean {
        UInspector.init(requireNotNull(context))
        return true
    }

    override fun query(uri: Uri, strings: Array<String?>?, s: String?, strings1: Array<String?>?, s1: String?): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String?>?): Int {
        return 0
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String?>?): Int {
        return 0
    }
}