package com.olekdia.mvp

interface IComponentFactory {
    fun <T : IComponent> construct(componentId: String): T
}