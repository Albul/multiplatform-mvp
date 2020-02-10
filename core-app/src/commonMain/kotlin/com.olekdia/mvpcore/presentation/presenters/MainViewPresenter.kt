package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.mvp.presenter.IViewPresenter
import com.olekdia.mvp.presenter.StatelessViewPresenter
import com.olekdia.mvpcore.platform.views.IMainView

interface IMainViewPresenter : IViewPresenter<IMainView> {
    fun showView(componentId: String, vararg params: Pair<String, Any?>)

    companion object {
        const val COMPONENT_ID = "MAIN_VIEW_PRES"
    }
}

class MainViewPresenter : StatelessViewPresenter<IMainView>(),
    IMainViewPresenter {

    override val componentId: String
        get() = IMainViewPresenter.COMPONENT_ID

    override fun showView(componentId: String, vararg params: Pair<String, Any?>) {
        view?.showView(componentId, *params)
    }
}