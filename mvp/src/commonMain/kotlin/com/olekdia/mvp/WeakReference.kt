package com.olekdia.mvp

expect class WeakReference<T : Any>(referred: T) {
    fun clear()
    fun get(): T?
}