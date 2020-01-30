package com.olekdia.mvp.presenter

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory

interface IPresenterProvider {

    var factory: IComponentFactory

    fun <T : IBasePresenter> get(componentId: String): T?

    fun <T : IBasePresenter> get(component: IComponent): T?

    fun remove(component: IComponent)

    fun remove(componentId: String)
}