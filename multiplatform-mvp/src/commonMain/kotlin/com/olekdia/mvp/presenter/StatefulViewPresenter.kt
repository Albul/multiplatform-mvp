package com.olekdia.mvp.presenter

abstract class StatefulViewPresenter<V : Any, S : Any> : BaseViewPresenter<V>(),
    IStatefulViewPresenter<V, S> {

    override fun onRestoreState(state: S) {
    }
}