package com.olekdia.mvpcore.common.extensions

inline fun <reified T : Enum<T>> T.next(): T =
    enumValues<T>().let { values: Array<T> ->
        values.indexOf(this).let { index ->
            when {
                values.size <= 1 -> this
                index > 0 -> values[0]
                else -> values[values.lastIndex]
            }
        }
    }