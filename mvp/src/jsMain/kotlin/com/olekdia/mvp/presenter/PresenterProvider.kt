package com.olekdia.mvp.presenter

import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvp.model.IModelProvider
import com.olekdia.mvp.model.ModelProvider

actual class PresenterProvider(
    presenterFactory: IComponentFactory,
    modelFactory: IComponentFactory
) : BasePresenterProvider(presenterFactory, modelFactory) {

    private val _modelProvider: ModelProvider = ModelProvider(this, modelFactory)

    override val modelProvider: IModelProvider
        get() = _modelProvider

    override fun IBasePresenter.configure() {
    }
}