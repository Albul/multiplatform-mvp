package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.mvp.ISingleComponentFactory
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpcore.Key
import com.olekdia.mvpcore.Param
import com.olekdia.mvpcore.ViewType
import com.olekdia.mvpcore.platform.view.views.IDiscardDialogView
import com.olekdia.mvpcore.platform.view.views.IInputTaskView
import com.olekdia.mvpcore.presentation.ExtPresenter

interface IDialogPresenter : IPresenter {

    fun onShowDiscardDlg(componentId: String)

    fun onDiscardDlg(componentId: String, @Param param: String)

    companion object FACTORY : ISingleComponentFactory<IPresenter> {
        const val COMPONENT_ID: String = "DIALOG_PRES"

        override fun invoke(): IPresenter = DialogPresenter()
    }
}

class DialogPresenter : ExtPresenter(), IDialogPresenter {

    override val componentId: String
        get() = IDialogPresenter.COMPONENT_ID

    override fun onShowDiscardDlg(componentId: String) {
        mainViewPresenter.showView(
            IDiscardDialogView.COMPONENT_ID,
            ViewType.DIALOG,
            arrayOf(Key.ID to componentId)
        )
    }

    override fun onDiscardDlg(componentId: String, @Param param: String) {
        when (componentId) {
            IInputTaskView.COMPONENT_ID -> {
                when (param) {
                    Param.DISCARD -> {
                        presenterProvider
                            .get<IInputTaskPresenter>(IInputTaskPresenter.COMPONENT_ID)!!
                            .onDiscard()
                    }
                    Param.SAVE -> {
                        presenterProvider
                            .get<IInputTaskPresenter>(IInputTaskPresenter.COMPONENT_ID)
                            ?.onApply()
                    }
                }
            }
        }
    }
}