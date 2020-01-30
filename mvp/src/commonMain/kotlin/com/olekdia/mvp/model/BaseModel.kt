package com.olekdia.mvp.model

import com.olekdia.mvp.presenter.IPresenterProvider

abstract class BaseModel : IBaseModel {

    lateinit var presenterProvider: IPresenterProvider
        internal set

    lateinit var modelProvider: IModelProvider
        internal set

    override val platformModel: IPlatformModel?
        get() = null

    override fun onCreate() {
    }

    override fun onDestroy() {
        presenterProvider.remove(this)
    }
}