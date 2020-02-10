package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.mvp.presenter.IViewPresenter
import com.olekdia.mvp.presenter.StatelessViewPresenter
import com.olekdia.mvpcore.platform.repositories.IPrefRepository
import com.olekdia.mvpcore.platform.managers.PrefManager
import com.olekdia.mvpcore.platform.views.IMainApp

interface IMainAppPresenter : IViewPresenter<IMainApp> {
    fun onAppInit()

    fun onConfigurationChanged()

    companion object {
        const val COMPONENT_ID = "MAIN_APP_PRES"
    }
}

class MainAppPresenter : StatelessViewPresenter<IMainApp>(),
    IMainAppPresenter {

    override val componentId: String
        get() = IMainAppPresenter.COMPONENT_ID

    override fun onAppInit() {
        PrefManager.pref = platformProvider.get(IPrefRepository.COMPONENT_ID)!!
        // todo init rest presenter

        view?.initApp()
    }

    override fun onConfigurationChanged() {
        view?.updateConfiguration()
    }
}