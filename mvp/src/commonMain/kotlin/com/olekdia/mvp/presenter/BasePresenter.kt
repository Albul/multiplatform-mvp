package com.olekdia.mvp.presenter

import com.olekdia.mvp.model.IModelProvider

abstract class BasePresenter : IBasePresenter {

    lateinit var presenterProvider: IPresenterProvider
        internal set

    lateinit var modelProvider: IModelProvider
        internal set

    override val platformPresenter: IPlatformPresenter?
        get() = null

    override fun onCreate() {
    }

    override fun onDestroy() {
        presenterProvider.remove(this)
    }
}