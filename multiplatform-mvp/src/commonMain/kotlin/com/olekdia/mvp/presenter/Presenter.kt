package com.olekdia.mvp.presenter

import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.model.IModel
import com.olekdia.mvp.platform.IPlatformComponent

abstract class Presenter : IPresenter {

    lateinit var modelProvider: IComponentProvider<IModel>
        internal set

    lateinit var presenterProvider: IComponentProvider<IPresenter>
        internal set

    lateinit var platformProvider: IComponentProvider<IPlatformComponent>
        internal set

    override fun onCreate() {
    }

    override fun onDestroy() {
        presenterProvider.remove(this)
    }
}