package com.olekdia.mvp

/**
 * Component factory interface.
 */
interface IComponentFactory {
    fun <T : IComponent> construct(componentId: String): T
}