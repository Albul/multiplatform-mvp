package com.olekdia.sample.presenter.presenters

import com.olekdia.mvp.framework.IBaseFwc
import com.olekdia.mvp.presenter.BasePresenter
import com.olekdia.mvp.presenter.IBasePresenter
import com.olekdia.sample.PresenterId
import com.olekdia.sample.ViewId

interface IToastManager : IBaseFwc {

    fun showToast(text: CharSequence)

    fun showTaskCreatedToast()
    fun showTaskSavedToast()

    companion object {
        const val COMPONENT_ID: String = ViewId.TOAST
    }
}

interface IToastPresenter : IBasePresenter {

    fun onShowToast(text: CharSequence)

    fun onTaskCreated()

    fun onTaskSaved()

    companion object {
        const val COMPONENT_ID: String = PresenterId.TOAST
    }
}

class ToastPresenter : BasePresenter(), IToastPresenter {

    override fun onShowToast(text: CharSequence) {
        toastMng?.showToast(text)
    }

    override fun onTaskCreated() {
        toastMng?.showTaskCreatedToast()
    }

    override fun onTaskSaved() {
        toastMng?.showTaskSavedToast()
    }

    override val componentId: String
        get() = IToastPresenter.COMPONENT_ID

    private val toastMng: IToastManager?
        get() = fwcProvider.get(IToastManager.COMPONENT_ID)
}