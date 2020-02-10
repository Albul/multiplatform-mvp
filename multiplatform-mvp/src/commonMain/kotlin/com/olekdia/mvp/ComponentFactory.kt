package com.olekdia.mvp

open class ComponentFactory<T : ILifecycleComponent>(
    private val factoryMap: MutableMap<String, ISingleComponentFactory<T>>
) : IComponentFactory<T> {

    override fun create(componentId: String): T? =
        factoryMap[componentId]?.invoke()

    override fun load(componentFactories: Array<Pair<String, ISingleComponentFactory<T>>>) {
        factoryMap.putAll(componentFactories)
    }

    override fun unload(componentIds: Array<String>) {
        for (id in componentIds) {
            factoryMap.remove(id)
        }
    }
}