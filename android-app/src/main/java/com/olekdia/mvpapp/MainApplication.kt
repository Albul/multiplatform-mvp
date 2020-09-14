package com.olekdia.mvpapp

import android.content.res.Configuration
import com.olekdia.mvp.Facade
import com.olekdia.mvp.IMutableComponentProvider
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpapp.presentation.PlatformComponentFactory
import com.olekdia.mvpcore.domain.ModelFactory
import com.olekdia.mvpcore.presentation.views.IMainApp
import com.olekdia.mvpcore.presentation.PresenterFactory
import com.olekdia.mvpcore.presentation.presenters.IMainAppPresenter

class MainApplication : MvpApplication(),
    IMainApp {

    override val componentId: String
        get() = IMainApp.COMPONENT_ID

    private val facade: Facade by lazy {
        Facade(
            ModelFactory(),
            PresenterFactory(),
            PlatformComponentFactory(baseContext)
        )
    }

    override val presenterProvider: IMutableComponentProvider<IPresenter>
        get() = facade.presenterProvider

    override fun initApp() {
        // App specific startup logic
    }

    override fun updateConfiguration() {
        // App specific logic
    }

    private val mainAppPresenter: IMainAppPresenter
        get() = presenterProvider
            .getOrCreate(IMainAppPresenter.COMPONENT_ID)!!

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