package com.olekdia.mvp.presenter

import com.olekdia.mvp.IMutableComponentProvider
import com.olekdia.mvp.model.IModel
import com.olekdia.mvp.model.IModelHolder
import com.olekdia.mvp.platform.IPlatformHolder
import com.olekdia.mvp.platform.IPlatformComponent

abstract class Presenter : IPresenter,
    IModelHolder,
    IPresenterHolder,
    IPlatformHolder {

    override lateinit var modelProvider: IMutableComponentProvider<IModel>
        internal set

    override lateinit var presenterProvider: IMutableComponentProvider<IPresenter>
        internal set

    override lateinit var platformProvider: IMutableComponentProvider<IPlatformComponent>
        internal set

    override fun onCreate() {
    }

    override fun onDestroy() {
        presenterProvider.remove(this)
    }
}