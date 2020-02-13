package com.olekdia.mvp

interface IComponentProvider<T : ILifecycleComponent> {

    /**
     * @return previously existing component, or new component if it wasn't created before
     */
    fun <C : T> get(componentId: String): C?

    /**
     * @return previously existing component, or new component if it wasn't created before.
     * It will return different instances for different parameters with same id
     */
    fun <C : T> get(componentId: String, param: String?): C?

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