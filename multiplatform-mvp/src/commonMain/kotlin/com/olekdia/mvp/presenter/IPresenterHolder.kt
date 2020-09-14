package com.olekdia.mvp.presenter

import com.olekdia.mvp.IMutableComponentProvider

interface IPresenterHolder {
    val presenterProvider: IMutableComponentProvider<IPresenter>
}