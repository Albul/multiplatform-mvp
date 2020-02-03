package com.olekdia.sample.presenter.presenters

import com.olekdia.mvp.EmptyState
import com.olekdia.mvp.presenter.IViewPresenter
import com.olekdia.mvp.presenter.ViewPresenter
import com.olekdia.sample.PresenterId
import com.olekdia.sample.ViewId

interface IMainView {
    fun showView(@ViewId componentId: String, vararg params: Pair<String, Any?>)
}

interface IMainViewPresenter : IViewPresenter<IMainView, EmptyState> {
    fun showView(componentId: String, vararg params: Pair<String, Any?>)

    companion object {
        const val COMPONENT_ID = PresenterId.MAIN_VIEW
    }
}

class MainViewPresenter : ViewPresenter<IMainView, EmptyState>(),
    IMainViewPresenter {
    override val componentId: String =
        IMainViewPresenter.COMPONENT_ID

    override var state: EmptyState = EmptyState

    override fun showView(componentId: String, vararg params: Pair<String, Any?>) {
        view?.showView(componentId, *params)
    }
}