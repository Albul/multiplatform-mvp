package com.olekdia.mvpcore.mocks

import com.olekdia.mvp.platform.PlatformComponent
import com.olekdia.mvpcore.platform.repositories.IPrefRepository

class PrefRepositoryMock : PlatformComponent(),
    IPrefRepository {

    private val pref: HashMap<String, Any?> = hashMapOf()

    override val componentId: String
        get() = IPrefRepository.COMPONENT_ID

//--------------------------------------------------------------------------------------------------
//  Get
//--------------------------------------------------------------------------------------------------

    override fun get(key: String, defValue: Boolean): Boolean =
        pref[key] as? Boolean ?: defValue

    override fun get(key: String, defValue: Int): Int =
        pref[key] as? Int ?: defValue

    override fun get(key: String, defValue: Long): Long =
        pref[key] as? Long ?: defValue

    override fun get(key: String, defValue: Float): Float =
        pref[key] as? Float ?: defValue

    override fun get(key: String, defValue: String): String? =
        pref[key] as? String ?: defValue

    override fun get(key: String, defValues: Set<String>?): Set<String>? =
        pref[key] as? Set<String> ?: defValues

//--------------------------------------------------------------------------------------------------
//  Set
//--------------------------------------------------------------------------------------------------

    override operator fun set(key: String, value: Boolean) {
        pref[key] = value
    }
    override operator fun set(key: String, value: Float) {
        pref[key] = value
    }
    override operator fun set(key: String, value: Int) {
        pref[key] = value
    }
    override operator fun set(key: String, value: Long) {
        pref[key] = value
    }
    override operator fun set(key: String, value: String) {
        pref[key] = value
    }
    override operator fun set(key: String, value: Set<String>?) {
        pref[key] = value
    }
}