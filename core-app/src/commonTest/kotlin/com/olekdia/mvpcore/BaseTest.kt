package com.olekdia.mvpcore

import com.olekdia.mvp.Facade
import com.olekdia.mvp.IMutableComponentProvider
import com.olekdia.mvp.model.IModel
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpcore.domain.IExtModelHolder
import com.olekdia.mvpcore.domain.ModelFactory
import com.olekdia.mvpcore.domain.repositories.IPrefsRepository
import com.olekdia.mvpcore.mocks.PlatformFactoryMock
import com.olekdia.mvpcore.presentation.IExtPlatformHolder
import com.olekdia.mvpcore.presentation.IExtPresenterHolder
import com.olekdia.mvpcore.presentation.PresenterFactory
import com.olekdia.mvpcore.presentation.managers.ISnackManager
import com.olekdia.mvpcore.presentation.managers.IToastManager
import com.olekdia.mvpcore.presentation.presenters.IMainAppPresenter
import com.olekdia.mvpcore.presentation.singletons.AppPrefs
import io.mockk.mockk

abstract class BaseTest : IExtModelHolder,
    IExtPresenterHolder,
    IExtPlatformHolder {

    val facade: Facade = Facade(
        ModelFactory(),
        PresenterFactory(),
        PlatformFactoryMock()
    )

    final override val presenterProvider: IMutableComponentProvider<IPresenter>
        get() = facade.presenterProvider

    final override val modelProvider: IMutableComponentProvider<IModel>
        get() = facade.modelProvider

    final override val platformProvider: IMutableComponentProvider<IPlatformComponent>
        get() = facade.platformProvider

    init {
        AppPrefs.prefs = platformProvider.getOrCreate(
            IPrefsRepository.COMPONENT_ID)!!

        presenterProvider
            .getOrCreate<IMainAppPresenter>(IMainAppPresenter.COMPONENT_ID)!!
            .onAppInit()

        // Simulate TaskListView, as it should be initialized during startup
        taskListPresenter.onAttach(mockk(relaxed = true, relaxUnitFun = true))
    }

    val toastMng: IToastManager
        get() = platformProvider.getOrCreate(IToastManager.COMPONENT_ID)!!

    val snackMng: ISnackManager
        get() = platformProvider.getOrCreate(ISnackManager.COMPONENT_ID)!!
}