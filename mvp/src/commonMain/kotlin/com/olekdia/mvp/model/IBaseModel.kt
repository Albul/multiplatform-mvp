package com.olekdia.mvp.model

import com.olekdia.mvp.IComponent

interface IBaseModel : IComponent {

    val platformModel: IPlatformModel?
        get() = null

    fun onCreate()

    fun onDestroy()
}