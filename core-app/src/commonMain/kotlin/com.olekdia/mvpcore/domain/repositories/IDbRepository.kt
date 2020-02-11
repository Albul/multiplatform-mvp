package com.olekdia.mvpcore.domain.repositories

import com.olekdia.mvp.platform.IPlatformComponent

interface IDbRepository : IPlatformComponent {

    fun delete(table: String, id: Int)

    fun delete(table: String, id: Long)

    fun delete(table: String, whereClause: String)

    companion object {
        const val COMPONENT_ID: String = "DB_REP"
    }
}