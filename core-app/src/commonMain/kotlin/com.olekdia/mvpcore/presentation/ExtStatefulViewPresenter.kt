package com.olekdia.mvpcore.presentation

import com.olekdia.mvp.presenter.StatefulViewPresenter
import com.olekdia.mvpcore.platform.managers.ITextManager
import com.olekdia.mvpcore.presentation.presenters.IMainViewPresenter
import com.olekdia.mvpcore.presentation.presenters.ISnackPresenter
import com.olekdia.mvpcore.presentation.presenters.IToastPresenter

abstract class ExtStatefulViewPresenter<V : Any, S : Any> : StatefulViewPresenter<V, S>() {

    val mainViewPresenter: IMainViewPresenter
        get() = presenterProvider.get(IMainViewPresenter.COMPONENT_ID)!!

    val toastPresenter: IToastPresenter
        get() = presenterProvider.get(IToastPresenter.COMPONENT_ID)!!

    val snackPresenter: ISnackPresenter
        get() = presenterProvider.get(ISnackPresenter.COMPONENT_ID)!!

    val textManager: ITextManager
        get() = platformProvider.get(ITextManager.COMPONENT_ID)!!
}