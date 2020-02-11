package com.olekdia.mvp.presenter

import com.olekdia.mvp.IComponentProvider

interface IPresenterHolder {
    val presenterProvider: IComponentProvider<IPresenter>
}