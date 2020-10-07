package com.olekdia.mvp

import com.olekdia.mvp.factories.ModelMockFactory
import com.olekdia.mvp.factories.PlatformMockFactory
import com.olekdia.mvp.factories.PresenterMockFactory
import com.olekdia.mvp.mocks.*
import com.olekdia.mvp.model.IModel
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvp.presenter.IPresenter

abstract class BaseTest {

    val facade: Facade = Facade(
        ModelMockFactory(),
        PresenterMockFactory(),
        PlatformMockFactory()
    )

    final val presenterProvider: IMutableComponentProvider<IPresenter>
        get() = facade.presenterProvider

    final val modelProvider: IMutableComponentProvider<IModel>
        get() = facade.modelProvider

    final val platformProvider: IMutableComponentProvider<IPlatformComponent>
        get() = facade.platformProvider

    fun retrieveMockModel(): IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID)
    fun retrieveMockPresenter(): IMockPresenter = presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID)
}