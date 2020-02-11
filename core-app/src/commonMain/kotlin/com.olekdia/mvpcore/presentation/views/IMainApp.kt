package com.olekdia.mvpcore.presentation.views

import com.olekdia.mvp.IComponent

interface IMainApp : IComponent {
    fun initApp()

    fun updateConfiguration()

    companion object {
        const val COMPONENT_ID: String = "MAIN_APP"
    }
}