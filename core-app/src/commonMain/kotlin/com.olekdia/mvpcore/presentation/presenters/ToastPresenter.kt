package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.mvp.presenter.BasePresenter
import com.olekdia.mvp.presenter.IBasePresenter
import com.olekdia.mvpcore.platform.managers.IToastManager

interface IToastPresenter : IBasePresenter {

    fun onShow(text: CharSequence)

    fun onHide()

    override val componentId: String
        get() = COMPONENT_ID

    companion object {
        const val COMPONENT_ID: String = "TOAST_PRES"
    }
}

class ToastPresenter : BasePresenter(), IToastPresenter {

    override fun onShow(text: CharSequence) {
        toastMng?.show(text)
    }

    override fun onHide() {
        toastMng?.hide()
    }

    private val toastMng: IToastManager?
        get() = platformProvider.get(IToastManager.COMPONENT_ID)
}