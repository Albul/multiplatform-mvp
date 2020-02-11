package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.mvp.presenter.IViewPresenter
import com.olekdia.mvp.presenter.StatelessViewPresenter
import com.olekdia.mvpcore.ViewType
import com.olekdia.mvpcore.presentation.views.IMainView

interface IMainViewPresenter : IViewPresenter<IMainView> {

    /**
     * @return true if view is shown, false otherwise
     */
    fun showView(
        componentId: String,
        viewType: ViewType,
        params: Array<Pair<String, Any?>>
    ): Boolean

    companion object {
        const val COMPONENT_ID = "MAIN_VIEW_PRES"
    }
}

class MainViewPresenter : StatelessViewPresenter<IMainView>(),
    IMainViewPresenter {

    override val componentId: String
        get() = IMainViewPresenter.COMPONENT_ID

    override fun showView(
        componentId: String,
        viewType: ViewType,
        params: Array<Pair<String, Any?>>
    ): Boolean =
        view?.showView(componentId, viewType, params) == true
}