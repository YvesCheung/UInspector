package com.pitaya.mobile.uinspector.util

/**
 * Indicate the parameter is passed from outside
 *
 * ```kotlin
 * class Fragment {
 *
 *     @Input
 *     var callback: OnClickListener? = null
 *
 *     //....
 * }
 * ```
 *
 * @see Output
 *
 * @author YvesCheung
 * 2020/11/11
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class Input