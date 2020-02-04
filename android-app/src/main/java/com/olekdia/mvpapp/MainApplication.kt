package com.olekdia.mvpapp

import android.content.res.Configuration
import com.olekdia.mvp.presenter.IPresenterProvider
import com.olekdia.mvp.presenter.PresenterProvider
import com.olekdia.mvpapp.platform.PlatformComponentFactory
import com.olekdia.mvpcore.model.ModelFactory
import com.olekdia.mvpcore.presentation.PresenterFactory
import com.olekdia.mvpcore.platform.views.IMainApp
import com.olekdia.mvpcore.presentation.presenters.IMainAppPresenter

class MainApplication : MvpApplication(), IMainApp {

    override val componentId: String
        get() = IMainApp.COMPONENT_ID

    override val presenterProvider: IPresenterProvider by lazy {
        PresenterProvider(
            PresenterFactory(),
            ModelFactory(),
            PlatformComponentFactory(baseContext)
        )
    }

    override fun initApp() {
        // todo app specific logic
    }

    override fun updateConfiguration() {
        // todo app specific logic
    }

    private val mainAppPresenter: IMainAppPresenter
        get() = presenterProvider
            .get(IMainAppPresenter.COMPONENT_ID)!!

    override fun onCreate() {
        super.onCreate()
        mainAppPresenter.let {
            it.onAttach(this)
            it.onAppInit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        mainAppPresenter.onConfigurationChanged()
    }
}