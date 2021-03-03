package com.pitaya.mobile.uinspector.optional

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.annotation.RestrictTo
import com.pitaya.mobile.uinspector.UInspector

/**
 * Auto install the [UInspector].
 *
 * You can disable this feature by excluding this module in `build.gradle`:
 *
 * ```groovy
 * dependencies {
 *     debugImplementation('com.pitaya.mobile:Uinspector:x.y.z') {
 *         exclude module: 'Uinspector-optional-autoinstall'
 *     }
 * }
 * ```
 *
 * @since 1.0.9
 * @author YvesCheung
 * 2020/12/28
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class UInspectorInstaller : ContentProvider() {

    override fun onCreate(): Boolean {
        UInspector.create(requireNotNull(context))
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