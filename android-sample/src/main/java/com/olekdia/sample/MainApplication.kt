package com.olekdia.sample

import com.olekdia.mvp.platform.MvpApplication
import com.olekdia.mvp.presenter.IPresenterProvider
import com.olekdia.mvp.presenter.PresenterProvider
import com.olekdia.sample.model.ModelFactory
import com.olekdia.sample.presenter.PresenterFactory

class MainApplication : MvpApplication() {

    override val presenterProvider: IPresenterProvider by lazy {
        PresenterProvider(PresenterFactory(), ModelFactory(baseContext))
    }
}