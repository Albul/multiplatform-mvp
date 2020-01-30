package com.olekdia.mvp.model

import android.content.Context
import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvp.presenter.IPresenterProvider

actual class ModelProvider(
    presenterProvider: IPresenterProvider,
    factory: IComponentFactory,
    val context: Context
) : BaseModelProvider(presenterProvider, factory) {

    override fun IBaseModel.configure() {
        this.platformModel?.context = context
    }
}