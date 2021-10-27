package com.olekdia.mvpcore.presentation

import com.olekdia.mvp.presenter.IPresenterHolder
import com.olekdia.mvpcore.presentation.presenters.*

interface IExtPresenterHolder : IPresenterHolder {

    val mainViewPresenter: IMainViewPresenter
        get() = presenterProvider.getOrCreate(IMainViewPresenter.COMPONENT_ID)

    val toastPresenter: IToastPresenter
        get() = presenterProvider.getOrCreate(IToastPresenter.COMPONENT_ID)

    val snackPresenter: ISnackPresenter
        get() = presenterProvider.getOrCreate(ISnackPresenter.COMPONENT_ID)

    val dialogPresenter: IDialogPresenter
        get() = presenterProvider.getOrCreate(IDialogPresenter.COMPONENT_ID)



    val inputTaskPresenter: IInputTaskPresenter
        get() = presenterProvider.getOrCreate(IInputTaskPresenter.COMPONENT_ID)

    val taskListPresenter: ITaskListPresenter
        get() = presenterProvider.getOrCreate(ITaskListPresenter.COMPONENT_ID)
}