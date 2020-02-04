@file:JvmName("ListExt")
package com.olekdia.mvpcore.extensions

import kotlin.jvm.JvmName

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
    return map {
        if (block(it)) newValue else it
    }
}

fun <T> List<T>.plus(index: Int, element: T): List<T> {
    val result = ArrayList<T>(size + 1)
    result.addAll(this)
    result.add(index, element)
    return result
}