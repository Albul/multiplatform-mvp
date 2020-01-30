package com.olekdia.mvp.presenter

import android.content.Context
import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvp.model.ModelProvider
import com.olekdia.mvp.model.IModelProvider

actual class PresenterProvider(
    presenterFactory: IComponentFactory,
    modelFactory: IComponentFactory,
    private val context: Context
) : BasePresenterProvider(presenterFactory, modelFactory) {

    private var _modelProvider: IModelProvider = ModelProvider(this, modelFactory, context)
    override val modelProvider: IModelProvider
        get() = _modelProvider

    override fun IBasePresenter.configure() {
        this.platformPresenter?.context = context
    }
}