package com.olekdia.mvpcore.presentation

import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpcore.presentation.presenters.*

class PresenterFactory : ComponentFactory<IPresenter>(
    mutableMapOf(
        IMainAppPresenter.COMPONENT_ID to { MainAppPresenter() },
        IMainViewPresenter.COMPONENT_ID to { MainViewPresenter() },
        IToastPresenter.COMPONENT_ID to ::ToastPresenter,
        ISnackPresenter.COMPONENT_ID to ::SnackPresenter,

        IInputTaskPresenter.COMPONENT_ID to { InputTaskPresenter() },
        ITaskListPresenter.COMPONENT_ID to { TaskListPresenter() }
    )
)