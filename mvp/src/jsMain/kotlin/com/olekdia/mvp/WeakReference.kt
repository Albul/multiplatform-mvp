package com.olekdia.mvp

actual class WeakReference<T : Any> actual constructor(referred: T) {

    var reference: T? = referred

    actual fun clear() {
        reference = null
    }

    actual fun get(): T? = reference
}