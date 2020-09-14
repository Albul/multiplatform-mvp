package com.olekdia.mvpapp

import android.app.Application
import com.olekdia.mvp.IMutableComponentProvider
import com.olekdia.mvp.presenter.IPresenter

abstract class MvpApplication : Application() {
    abstract val presenterProvider: IMutableComponentProvider<IPresenter>
}