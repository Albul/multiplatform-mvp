package com.olekdia.mvp.model

import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvp.presenter.IPresenterProvider

actual class ModelProvider(
    presenterProvider: IPresenterProvider,
    factory: IComponentFactory
) : BaseModelProvider(presenterProvider, factory) {

    override fun IBaseModel.configure() {
    }
}