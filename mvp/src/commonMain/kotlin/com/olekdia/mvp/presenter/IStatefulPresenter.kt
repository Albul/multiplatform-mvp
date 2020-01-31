package com.olekdia.mvp.presenter

interface IStatefulPresenter<S> : IBasePresenter {
    val state: S

    fun onRestoreState(state: S)
}