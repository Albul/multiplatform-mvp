package com.olekdia.mvp.presenter

import com.olekdia.mvp.IComponent

interface IBasePresenter : IComponent {

    val platformPresenter: IPlatformPresenter?

    fun onCreate()

    fun onDestroy()
}