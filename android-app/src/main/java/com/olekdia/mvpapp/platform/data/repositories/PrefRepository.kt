package com.olekdia.mvpapp.platform.data.repositories

import android.content.SharedPreferences
import com.olekdia.mvp.platform.PlatformComponent
import com.olekdia.mvpcore.platform.data.repositories.IPrefRepository

class PrefRepository(private val pref: SharedPreferences) : PlatformComponent(),
    IPrefRepository {

    override val componentId: String
        get() = IPrefRepository.COMPONENT_ID

//--------------------------------------------------------------------------------------------------
//  Get
//--------------------------------------------------------------------------------------------------

    override fun get(key: String, defValue: Boolean): Boolean =
        pref.getBoolean(key, defValue)

    override fun get(key: String, defValue: Int): Int =
        pref.getInt(key, defValue)

    override fun get(key: String, defValue: Long): Long =
        pref.getLong(key, defValue)

    override fun get(key: String, defValue: Float): Float =
        pref.getFloat(key, defValue)

    override fun get(key: String, defValue: String): String? =
        pref.getString(key, defValue)

    override fun get(key: String, defValues: Set<String>?): Set<String>? =
        pref.getStringSet(key, defValues)

//--------------------------------------------------------------------------------------------------
//  Set
//--------------------------------------------------------------------------------------------------

    override operator fun set(key: String, value: Boolean) {
        pref.edit().putBoolean(key, value).apply()
    }
    override operator fun set(key: String, value: Float) {
        pref.edit().putFloat(key, value).apply()
    }
    override operator fun set(key: String, value: Int) {
        pref.edit().putInt(key, value).apply()
    }
    override operator fun set(key: String, value: Long) {
        pref.edit().putLong(key, value).apply()
    }
    override operator fun set(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }
    override operator fun set(key: String, value: Set<String>?) {
        pref.edit().putStringSet(key, value).apply()
    }
}