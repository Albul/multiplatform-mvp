package com.olekdia.sample.presenter

import com.olekdia.mvp.presenter.ViewPresenter
import com.olekdia.sample.presenter.presenters.IToastPresenter

abstract class ExtViewPresenter<V : Any, S : Any> : ViewPresenter<V, S>() {

    val toastPresenter: IToastPresenter?
        get() = presenterProvider.get(IToastPresenter.COMPONENT_ID)

}