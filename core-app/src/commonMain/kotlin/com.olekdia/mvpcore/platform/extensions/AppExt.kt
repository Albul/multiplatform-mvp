@file:JvmName("AppExt")
package com.olekdia.mvpcore.platform.extensions

import com.olekdia.mvpcore.NumEndingFormat
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

fun Int.getNumEndingFormat(): NumEndingFormat =
    when {
        this == 1 -> NumEndingFormat.ONE
        this.hasLast1Digit() -> NumEndingFormat.X1
        this.hasLast234Digit() -> NumEndingFormat.X4
        else -> NumEndingFormat.X5
    }

fun Int.hasLast1Digit(): Boolean =
    this > 20 && this % 10 == 1

fun Int.hasLast234Digit(): Boolean =
    (this % 10).let { rest10 ->
        this in 2..4 || rest10 in 2..4 && this > 20
    }