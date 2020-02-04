package com.olekdia.mvp.platform

import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvp.presenter.IPresenterProvider

class PlatformProvider(
    protected val presenterProvider: IPresenterProvider,
    override var factory: IComponentFactory
) : IPlatformProvider {

    protected val map: MutableMap<String, in IBasePlatformComponent> = hashMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <T : IBasePlatformComponent> get(componentId: String): T? =
        run {
            map[componentId]
                ?: create(componentId).also {
                    map[componentId] = it
                    it.onCreate()
                }
        } as? T

    private fun create(componentId: String): IBasePlatformComponent =
        factory.construct<IBasePlatformComponent>(componentId)
            .also { component ->
                (component as? BasePlatformComponent)?.let {
                    it.presenterProvider = presenterProvider
                    it.platformProvider = this
                }
            }

    override fun remove(component: IBasePlatformComponent) {
        remove(component.componentId)
    }

    override fun remove(componentId: String) {
        map.remove(componentId)
    }
}