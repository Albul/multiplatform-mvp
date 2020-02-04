package com.olekdia.mvp.platform

import com.olekdia.mvp.IComponentFactory

interface IPlatformProvider {

    var factory: IComponentFactory

    fun <T : IBasePlatformComponent> get(componentId: String): T?

    fun remove(component: IBasePlatformComponent)

    fun remove(componentId: String)
}