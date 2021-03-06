package com.olekdia.mvpcore.presentation.managers

import com.olekdia.mvp.platform.IPlatformComponent

interface IToastManager : IPlatformComponent {

    fun show(text: CharSequence)

    fun hide()

    companion object {
        const val COMPONENT_ID: String = "TOAST_MNG"
    }
}