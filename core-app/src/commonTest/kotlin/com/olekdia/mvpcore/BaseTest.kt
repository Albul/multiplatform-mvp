package com.olekdia.mvpcore

import com.olekdia.mvp.Facade
import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.model.IModel
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpcore.mocks.PlatformFactoryMock
import com.olekdia.mvpcore.domain.ModelFactory
import com.olekdia.mvpcore.presentation.singletons.AppPrefs
import com.olekdia.mvpcore.domain.repositories.IPrefsRepository
import com.olekdia.mvpcore.presentation.PresenterFactory
import com.olekdia.mvpcore.presentation.presenters.IMainAppPresenter
import com.olekdia.mvpcore.presentation.presenters.ITaskListPresenter
import io.mockk.mockk

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

    init {
        AppPrefs.prefs = platformProvider.get(
            IPrefsRepository.COMPONENT_ID)!!

        presenterProvider
            .get<IMainAppPresenter>(IMainAppPresenter.COMPONENT_ID)!!
            .onAppInit()

        // Simulate TaskListView, as it should be initialized during startup
        presenterProvider
            .get<ITaskListPresenter>(ITaskListPresenter.COMPONENT_ID)!!
            .onAttach(mockk(relaxed = true, relaxUnitFun = true))
    }
}