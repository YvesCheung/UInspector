package com.pitaya.mobile.uinspector.lifecycle

import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author YvesCheung
 * 2021/1/15
 */
interface Disposable {

    val isDisposed: Boolean

    fun dispose()

    companion object {

        fun create(doOnDispose: () -> Unit): Disposable {
            return object : AtomicBoolean(), Disposable {
                override val isDisposed: Boolean = get()

                override fun dispose() {
                    if (compareAndSet(false, true)) {
                        doOnDispose()
                    }
                }
            }
        }

        val EMPTY = object : Disposable {
            override val isDisposed: Boolean = true

            override fun dispose() {}
        }
    }
}