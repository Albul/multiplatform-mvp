package com.olekdia.mvp.presenter

interface IStatefulPresenter<S> : IBasePresenter {
    var state: S?
}