package com.olekdia.mvpapp

import com.olekdia.mvp.Facade
import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.model.IModel
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpapp.mocks.PlatformFactoryMock
import com.olekdia.mvpcore.model.ModelFactory
import com.olekdia.mvpcore.presentation.PresenterFactory

abstract class BaseTest {

    private val facade: Facade = Facade(
        ModelFactory(),
        PresenterFactory(),
        PlatformFactoryMock()
    )

    val presenterProvider: IComponentProvider<IPresenter>
        get() = facade.presenterProvider

    val modelProvider: IComponentProvider<IModel>
        get() = facade.modelProvider

    val platformProvider: IComponentProvider<IPlatformComponent>
        get() = facade.platformProvider
}