package com.olekdia.mvpcore.model.repositories

import com.olekdia.mvp.platform.IBasePlatformComponent

interface IPrefRepository : IBasePlatformComponent {

    operator fun get(key: String, defValue: Boolean): Boolean
    operator fun get(key: String, defValue: Int): Int
    operator fun get(key: String, defValue: Long): Long
    operator fun get(key: String, defValue: Float): Float
    operator fun get(key: String, defValue: String): String?
    operator fun get(key: String, defValues: Set<String>?): Set<String>?

    operator fun set(key: String, value: Boolean)
    operator fun set(key: String, value: Int)
    operator fun set(key: String, value: Long)
    operator fun set(key: String, value: Float)
    operator fun set(key: String, value: String)
    operator fun set(key: String, value: Set<String>?)

    companion object {
        const val COMPONENT_ID = "PREF_REP"
    }
}