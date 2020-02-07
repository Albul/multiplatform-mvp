package com.olekdia.mvpapp

import android.app.Application
import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.presenter.IPresenter

abstract class MvpApplication : Application() {
    abstract val presenterProvider: IComponentProvider<IPresenter>
}