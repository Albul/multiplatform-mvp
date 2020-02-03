package com.olekdia.sample.presenter

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.sample.PresenterId
import com.olekdia.sample.presenter.presenters.InputTaskPresenter
import com.olekdia.sample.presenter.presenters.MainViewPresenter
import com.olekdia.sample.presenter.presenters.TaskListPresenter
import com.olekdia.sample.presenter.presenters.ToastPresenter

class PresenterFactory : IComponentFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : IComponent> construct(@PresenterId componentId: String): T =
        when (componentId) {
            PresenterId.INPUT_TASK -> InputTaskPresenter()
            PresenterId.TASK_LIST -> TaskListPresenter()
            PresenterId.MAIN_VIEW -> MainViewPresenter()
            PresenterId.TOAST -> ToastPresenter()
            else -> throw RuntimeException("Presenter class not found")
        }.let { it as T }

}