package com.olekdia.mvp.platform

import android.app.Application
import com.olekdia.mvp.presenter.IPresenterProvider

abstract class MvpApplication : Application() {
    abstract val presenterProvider: IPresenterProvider
}