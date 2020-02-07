package com.olekdia.mvp

interface IComponentFactory<T : IComponent> {

    fun create(componentId: String): T?

    fun load(componentFactories: Array<Pair<String, ISingleComponentFactory<T>>>)

    fun unload(componentIds: Array<String>)
}