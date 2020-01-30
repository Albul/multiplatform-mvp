package com.olekdia.mvp.presenter

interface IViewPresenter<V, S> : IStatefulPresenter<S> {
    var view: V?

    fun onAttach(v: V)

    fun onStart()

    fun onStop()

    fun onDetach(v: V)
}