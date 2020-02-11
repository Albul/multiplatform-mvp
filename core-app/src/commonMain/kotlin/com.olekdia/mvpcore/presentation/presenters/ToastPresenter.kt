package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.mvp.presenter.Presenter
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpcore.presentation.managers.IToastManager

interface IToastPresenter : IPresenter {

    fun onShow(text: CharSequence)

    fun onHide()

    companion object {
        const val COMPONENT_ID: String = "TOAST_PRES"
    }
}

class ToastPresenter : Presenter(), IToastPresenter {

    override val componentId: String
        get() = IToastPresenter.COMPONENT_ID

    override fun onShow(text: CharSequence) {
        toastMng?.show(text)
    }

    override fun onHide() {
        toastMng?.hide()
    }

    private val toastMng: IToastManager?
        get() = platformProvider.get(IToastManager.COMPONENT_ID)
}