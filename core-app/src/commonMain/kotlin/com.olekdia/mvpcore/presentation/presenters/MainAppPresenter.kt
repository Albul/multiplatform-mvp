package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.mvp.presenter.IViewPresenter
import com.olekdia.mvp.presenter.StatelessViewPresenter
import com.olekdia.mvpcore.domain.IExtModelHolder
import com.olekdia.mvpcore.domain.repositories.IPrefsRepository
import com.olekdia.mvpcore.presentation.IExtPresenterHolder
import com.olekdia.mvpcore.presentation.singletons.AppPrefs
import com.olekdia.mvpcore.presentation.views.IMainApp

interface IMainAppPresenter : IViewPresenter<IMainApp> {
    fun onAppInit()

    fun onConfigurationChanged()

    companion object {
        const val COMPONENT_ID = "MAIN_APP_PRES"
    }
}

class MainAppPresenter : StatelessViewPresenter<IMainApp>(),
    IMainAppPresenter,
    IExtModelHolder,
    IExtPresenterHolder {

    override val componentId: String
        get() = IMainAppPresenter.COMPONENT_ID

    override fun onAppInit() {
        AppPrefs.prefs = platformProvider.getOrCreate(IPrefsRepository.COMPONENT_ID)!!

        // Load initial data
        taskModel.loadAsync {
            taskListPresenter.onUpdateView()
        }

        // Init rest of the presenter

        view?.initApp()
    }

    override fun onConfigurationChanged() {
        view?.updateConfiguration()
    }
}