package com.olekdia.mvpcore.presentation

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvpcore.presentation.presenters.*

class PresenterFactory : IComponentFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : IComponent> construct(componentId: String): T =
        when (componentId) {
            IMainAppPresenter.COMPONENT_ID -> MainAppPresenter()
            IMainViewPresenter.COMPONENT_ID -> MainViewPresenter()
            IToastPresenter.COMPONENT_ID -> ToastPresenter()
            ISnackPresenter.COMPONENT_ID -> SnackPresenter()

            IInputTaskPresenter.COMPONENT_ID -> InputTaskPresenter()
            ITaskListPresenter.COMPONENT_ID -> TaskListPresenter()
            else -> throw RuntimeException("Presenter class not found")
        }.let { it as T }
}