package com.olekdia.mvpcore.platform.view.managers

import com.olekdia.mvp.platform.IPlatformComponent

interface ITextManager : IPlatformComponent {

    val undo: String

    val taskCreated: String
    val taskSaved: String
    val taskDeleted: String

    fun xxTasksDeleted(number: Int): String

    companion object {
        const val COMPONENT_ID = "TEXT_RES_MNG"
    }
}