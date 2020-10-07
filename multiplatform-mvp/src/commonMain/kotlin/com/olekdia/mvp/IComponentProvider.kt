package com.olekdia.mvp

interface IComponentProvider<T : ILifecycleComponent> {

    /**
     * @return previously existing component, or new component if it wasn't created before
     */
    fun <C : T> getOrCreate(componentId: String): C

    /**
     * @return previously existing component, or new component if it wasn't created before.
     * It will return different instances for different parameters with same id
     */
    fun <C : T> getOrCreate(componentId: String, param: String?): C

    /**
     * @return previously existing component, or null if it wasn't created before
     */
    operator fun <C : T> get(componentId: String): C?

    /**
     * @return previously existing component, or null if it wasn't created before.
     */
    operator fun <C : T> get(componentId: String, param: String?): C?
}