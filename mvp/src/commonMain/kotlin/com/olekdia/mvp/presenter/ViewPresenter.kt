package com.olekdia.mvp.presenter

import com.olekdia.mvp.WeakReference

abstract class ViewPresenter<T : Any> : BasePresenter(), IViewPresenter<T> {

    private var _view: WeakReference<T>? = null

    override var view: T?
        get() = _view?.get()
        set(value) {
            _view = if (value == null) {
                null
            } else {
                WeakReference(value)
            }
        }

    override fun onAttach(v: T) {
        view = v
    }

    override fun onDetach(v: T) {
        if (v == view) {
            view = null
        }
    }
}