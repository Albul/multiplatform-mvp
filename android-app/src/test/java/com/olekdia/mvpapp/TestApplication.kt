package com.olekdia.mvpapp

import com.olekdia.mvp.Facade
import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpapp.presentation.PlatformComponentFactory
import com.olekdia.mvpcore.domain.ModelFactory
import com.olekdia.mvpcore.presentation.PresenterFactory

class TestApplication : MvpApplication() {

    private val facade: Facade by lazy {
        Facade(
            ModelFactory(),
            PresenterFactory(),
            PlatformComponentFactory(baseContext)
        )
    }

    override val presenterProvider: IComponentProvider<IPresenter>
        get() = facade.presenterProvider

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
    }
}