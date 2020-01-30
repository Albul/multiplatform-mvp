package com.olekdia.mvp.presenter

import com.olekdia.mvp.IComponent

interface IBasePresenter : IComponent {

    fun onCreate()

    fun onDestroy()
}