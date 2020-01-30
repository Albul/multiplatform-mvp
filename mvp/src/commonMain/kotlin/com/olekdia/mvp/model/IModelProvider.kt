package com.olekdia.mvp.model

import com.olekdia.mvp.IComponentFactory

interface IModelProvider {

    var factory: IComponentFactory

    fun <T : IBaseModel> get(componentId: String): T?

    fun remove(component: IBaseModel)

    fun remove(componentId: String)
}