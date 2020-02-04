package com.olekdia.mvpapp

import com.olekdia.mvp.model.IModelProvider
import com.olekdia.mvp.presenter.IPresenterProvider
import com.olekdia.mvp.presenter.PresenterProvider
import com.olekdia.mvpapp.mocks.PlatformFactoryMock
import com.olekdia.mvpapp.model.ModelFactory
import com.olekdia.mvpapp.presentation.PresenterFactory
import kotlin.reflect.jvm.isAccessible

abstract class BaseTest {

    val presenterProvider: IPresenterProvider = PresenterProvider(
        PresenterFactory(),
        ModelFactory(),
        PlatformFactoryMock()
    )

    val modelProvider: IModelProvider =
        PresenterProvider::class.members.find {
            it.isAccessible = true
            it.name == "modelProvider"
        }!!.call(presenterProvider) as IModelProvider
}