package com.olekdia.mvp.model

import com.olekdia.mvp.IComponent

interface IBaseModel : IComponent {

    fun onCreate()

    fun onDestroy()
}