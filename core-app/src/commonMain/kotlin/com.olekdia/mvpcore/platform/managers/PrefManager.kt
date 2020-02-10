@file:Suppress("NOTHING_TO_INLINE")
package com.olekdia.mvpcore.platform.managers

import com.olekdia.mvpcore.TaskFilter
import com.olekdia.mvpcore.domain.entries.PrefEntry
import com.olekdia.mvpcore.platform.repositories.IPrefRepository
import kotlin.jvm.JvmField

object PrefManager {

    lateinit var pref: IPrefRepository

    operator fun get(key: String, value: Boolean) = pref[key, value]
    operator fun get(key: String, value: Float) = pref[key, value]
    operator fun get(key: String, value: Int) = pref[key, value]
    operator fun get(key: String, value: Long) = pref[key, value]
    operator fun get(key: String, value: String): String? = pref[key, value]
    operator fun get(key: String, value: Set<String>): Set<String>? = pref[key, value]

    operator fun set(key: String, value: Boolean) { pref[key] = value }
    operator fun set(key: String, value: Float) { pref[key] = value }
    operator fun set(key: String, value: Int) { pref[key] = value }
    operator fun set(key: String, value: Long) { pref[key] = value }
    operator fun set(key: String, value: String) { pref[key] = value }
    operator fun set(key: String, value: Set<String>) { pref[key] = value }

    @JvmField
    val taskFilter = PrefEntry.EnumPref("taskFilter", TaskFilter.ALL.name)
}