package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.mvp.presenter.IViewPresenter
import com.olekdia.mvp.presenter.StatelessViewPresenter
import com.olekdia.mvpcore.domain.models.ITaskModel
import com.olekdia.mvpcore.platform.data.repositories.IPrefRepository
import com.olekdia.mvpcore.platform.view.managers.PrefManager
import com.olekdia.mvpcore.platform.view.views.IMainApp

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

        // Load initial data
        modelProvider
            .get<ITaskModel>(ITaskModel.COMPONENT_ID)!!
            .loadAsync {
                presenterProvider
                    .get<ITaskListPresenter>(ITaskListPresenter.COMPONENT_ID)!!
                    .onUpdateView()
            }

        // todo init rest presenter

        view?.initApp()
    }

    override fun onConfigurationChanged() {
        view?.updateConfiguration()
    }
}