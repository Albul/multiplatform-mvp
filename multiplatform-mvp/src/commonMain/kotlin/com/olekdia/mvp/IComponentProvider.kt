package com.olekdia.mvp

interface IComponentProvider<T : IComponent> {

    fun <C : T> get(componentId: String): C?

    fun <C : T> get(componentId: String, param: Any?): C?

    fun remove(component: T)

    fun remove(componentId: String)

    fun remove(componentId: String, param: Any?)
}