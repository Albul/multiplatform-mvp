package com.olekdia.sample

import com.olekdia.mvp.model.IModelProvider
import com.olekdia.mvp.presenter.IPresenterProvider
import com.olekdia.mvp.presenter.PresenterProvider
import com.olekdia.sample.presenter.ModelFactoryMock
import com.olekdia.sample.presenter.PresenterFactory
import kotlin.reflect.jvm.isAccessible

abstract class BaseTest {

    val presenterProvider: IPresenterProvider = PresenterProvider(
        PresenterFactory(),
        ModelFactoryMock()
    )

    val modelProvider: IModelProvider =
        PresenterProvider::class.members.find {
            it.isAccessible = true
            it.name == "modelProvider"
        }!!.call(presenterProvider) as IModelProvider
}