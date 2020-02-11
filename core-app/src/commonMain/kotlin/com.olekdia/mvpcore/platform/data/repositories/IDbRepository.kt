package com.olekdia.mvpcore.platform.data.repositories

import com.olekdia.mvp.platform.IPlatformComponent

interface IDbRepository : IPlatformComponent {

    companion object {
        const val COMPONENT_ID: String = "DB_REP"
    }
}