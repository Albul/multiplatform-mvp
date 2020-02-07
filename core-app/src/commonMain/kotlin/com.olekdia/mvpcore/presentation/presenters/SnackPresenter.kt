package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvp.presenter.StatelessViewPresenter
import com.olekdia.mvpcore.platform.managers.ISnackManager
import com.olekdia.mvpcore.platform.managers.OnSnackbarStateChangedListener

interface ISnackPresenter : IPresenter {

    fun onShow(
        content: CharSequence,
        action: CharSequence,
        listener: OnSnackbarStateChangedListener,
        inFormView: Boolean = true
    )

    fun onUndo()

    fun onApply()

    companion object {
        const val COMPONENT_ID: String = "SNACK_PRES"
    }
}

class SnackPresenter : StatelessViewPresenter<ISnackManager>(),
    ISnackPresenter {

    override fun onShow(
        content: CharSequence,
        action: CharSequence,
        listener: OnSnackbarStateChangedListener,
        inFormView: Boolean
    ) {
        snackMng.show(content, action, listener, inFormView)
    }

    override fun onUndo() {
        snackMng.undo()
    }

    override fun onApply() {
        snackMng.apply()
    }

    override val componentId: String
        get() = ISnackPresenter.COMPONENT_ID

    private val snackMng: ISnackManager
        get() = platformProvider.get(ISnackManager.COMPONENT_ID)!!
}