package com.olekdia.mvp.model

import com.olekdia.mvp.presenter.IPresenterProvider

abstract class BaseModel : IBaseModel {

    lateinit var presenterProvider: IPresenterProvider
        internal set

    lateinit var modelProvider: IModelProvider
        internal set

    override fun onCreate() {
    }

    override fun onDestroy() {
        modelProvider.remove(this)
    }
}