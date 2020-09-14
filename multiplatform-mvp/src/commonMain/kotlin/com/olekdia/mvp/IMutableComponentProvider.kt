package com.olekdia.mvp

interface IMutableComponentProvider<T : ILifecycleComponent> : IComponentProvider<T> {
    /**
     * Removes given instance of component, not important with what param it was populated
     */
    fun remove(component: T)

    /**
     * Removes instance of component by its id. param is treated as null
     */
    fun remove(componentId: String)

    /**
     * Removes instance of component by its id and param
     */
    fun remove(componentId: String, param: String?)
}