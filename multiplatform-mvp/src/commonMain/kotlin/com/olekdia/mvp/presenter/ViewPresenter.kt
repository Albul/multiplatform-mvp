package com.olekdia.mvp.presenter

import com.olekdia.common.WeakReference

abstract class ViewPresenter<V : Any> : Presenter(),
    IViewPresenter<V> {

    private var _view: WeakReference<V>? = null

    override var view: V?
        get() = _view?.get()
        protected set(value) {
            _view = if (value == null) {
                null
            } else {
                WeakReference(value)
            }
        }

    override fun onAttach(v: V) {
        view = v
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onDetach(v: V) {
        if (v == view) {
            view = null
        }
    }
}