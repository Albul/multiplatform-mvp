package com.olekdia.mvp

interface IComponentFactory<T : ILifecycleComponent> {

    fun create(componentId: String): T?

    fun load(componentFactories: Array<Pair<String, ISingleComponentFactory<T>>>)

    fun unload(componentIds: Array<String>)
}