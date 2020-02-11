@file:Suppress("NOTHING_TO_INLINE")
package com.olekdia.mvpcore.presentation.singletons

import com.olekdia.mvpcore.TaskFilter
import com.olekdia.mvpcore.domain.entries.PrefEntry
import com.olekdia.mvpcore.domain.repositories.IPrefsRepository
import kotlin.jvm.JvmField

object AppPrefs {

    lateinit var prefs: IPrefsRepository

    operator fun get(key: String, value: Boolean) = prefs[key, value]
    operator fun get(key: String, value: Float) = prefs[key, value]
    operator fun get(key: String, value: Int) = prefs[key, value]
    operator fun get(key: String, value: Long) = prefs[key, value]
    operator fun get(key: String, value: String): String? = prefs[key, value]
    operator fun get(key: String, value: Set<String>): Set<String>? = prefs[key, value]

    operator fun set(key: String, value: Boolean) { prefs[key] = value }
    operator fun set(key: String, value: Float) { prefs[key] = value }
    operator fun set(key: String, value: Int) { prefs[key] = value }
    operator fun set(key: String, value: Long) { prefs[key] = value }
    operator fun set(key: String, value: String) { prefs[key] = value }
    operator fun set(key: String, value: Set<String>) { prefs[key] = value }

    @JvmField
    val taskFilter = PrefEntry.EnumPref("taskFilter", TaskFilter.ALL.name)
}