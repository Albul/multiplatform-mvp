package com.olekdia.mvp.presenter

interface IViewPresenter<T> : IBasePresenter {
    var view: T?

    fun onAttach(v: T)

    fun onDetach(v: T)
}