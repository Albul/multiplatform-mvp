package com.olekdia.mvpcore.domain.entries

import com.olekdia.mvpcore.presentation.singletons.AppPrefs.prefs
import kotlin.jvm.JvmField

sealed class PrefEntry<T>(
    @JvmField val key: String,
    @JvmField val defValue: T
) {
    class BooleanPref(key: String, defValue: Boolean) : PrefEntry<Boolean>(key, defValue) {
        override var value: Boolean
            get() = prefs[key, defValue]
            set(value) {
                prefs[key] = value
            }
    }

    class IntPref(key: String, defValue: Int) : PrefEntry<Int>(key, defValue) {
        override var value: Int
            get() = prefs[key, defValue]
            set(value) {
                prefs[key] = value
            }
    }

    class LongPref(key: String, defValue: Long) : PrefEntry<Long>(key, defValue) {
        override var value: Long
            get() = prefs[key, defValue]
            set(value) {
                prefs[key] = value
            }
    }

    class FloatPref(key: String, defValue: Float) : PrefEntry<Float>(key, defValue) {
        override var value: Float
            get() = prefs[key, defValue]
            set(value) {
                prefs[key] = value
            }
    }

    class StringPref(key: String, defValue: String) : PrefEntry<String>(key, defValue) {
        override var value: String
            get() = prefs[key, defValue] ?: defValue
            set(value) {
                prefs[key] = value
            }
    }

    class EnumPref(key: String, defValue: String) : PrefEntry<String>(key, defValue) {
        override var value: String
            get() = prefs[key, defValue] ?: defValue
            set(value) {
                prefs[key] = value
            }

        inline fun <reified T : Enum<T>> getEnumValue(): T =
            enumValueOf<T>(value)

        fun setEnumValue(enumValue: Enum<*>) {
            value = enumValue.name
        }
    }

    class StringSetPref(key: String, defValue: Set<String>) : PrefEntry<Set<String>>(key, defValue) {
        override var value: Set<String>
            get() = prefs[key, defValue] ?: defValue
            set(value) {
                prefs[key] = value
            }
        override val valueSerialized: String
            get() = value.joinToString()
    }

    abstract var value: T

    open val valueSerialized: String
        get() = value.toString()

    fun setDefValue() {
        value = defValue
    }
}