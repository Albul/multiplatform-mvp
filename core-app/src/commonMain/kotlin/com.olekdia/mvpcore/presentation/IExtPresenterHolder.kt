package com.olekdia.mvpcore.presentation

import com.olekdia.mvp.presenter.IPresenterHolder
import com.olekdia.mvpcore.presentation.presenters.*

interface IExtPresenterHolder : IPresenterHolder {

    val mainViewPresenter: IMainViewPresenter
        get() = presenterProvider.get(IMainViewPresenter.COMPONENT_ID)!!

    val toastPresenter: IToastPresenter
        get() = presenterProvider.get(IToastPresenter.COMPONENT_ID)!!

    val snackPresenter: ISnackPresenter
        get() = presenterProvider.get(ISnackPresenter.COMPONENT_ID)!!

    val dialogPresenter: IDialogPresenter
        get() = presenterProvider.get(IDialogPresenter.COMPONENT_ID)!!



    val inputTaskPresenter: IInputTaskPresenter
        get() = presenterProvider.get(IInputTaskPresenter.COMPONENT_ID)!!

    val taskListPresenter: ITaskListPresenter
        get() = presenterProvider.get(ITaskListPresenter.COMPONENT_ID)!!
}