package com.olekdia.mvpcore.platform.managers

import com.olekdia.mvp.platform.IBasePlatformComponent

interface IToastManager : IBasePlatformComponent {

    fun show(text: CharSequence)

    fun hide()

    companion object {
        const val COMPONENT_ID: String = "TOAST_MNG"
    }
}