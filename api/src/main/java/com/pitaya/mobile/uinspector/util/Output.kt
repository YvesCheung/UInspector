package com.pitaya.mobile.uinspector.util

/**
 * Indicate the parameter is the result of executing method.
 *
 * ```kotlin
 * fun getVisibleRect(@Output rect: Rect) {
 *       rect.left = 10
 *       rect.bottom = 20
 * }
 * ```
 *
 * @see Input
 *
 * @author YvesCheung
 * 2020/11/11
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class Output