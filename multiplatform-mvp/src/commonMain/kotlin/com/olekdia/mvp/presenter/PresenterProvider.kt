package com.olekdia.mvp.presenter

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvp.platform.PlatformProvider
import com.olekdia.mvp.platform.IPlatformProvider
import com.olekdia.mvp.model.IModelProvider
import com.olekdia.mvp.model.ModelProvider

class PresenterProvider(
    presenterFactory: IComponentFactory,
    modelFactory: IComponentFactory,
    platformFactory: IComponentFactory
) : IPresenterProvider {

    protected val platformProvider: IPlatformProvider =
        PlatformProvider(this, platformFactory)
    protected val modelProvider: IModelProvider = ModelProvider(this, platformProvider, modelFactory)

    protected val map: MutableMap<String, in IBasePresenter> = hashMapOf()

    private var _factory: IComponentFactory = presenterFactory
    override var factory: IComponentFactory
        get() = _factory
        set(value) {
            _factory = value
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : IBasePresenter> get(componentId: String): T? =
        run {
            map[componentId]
                ?: create(componentId).also {
                    map[componentId] = it
                    it.onCreate()
                }
        } as? T

    private fun create(componentId: String): IBasePresenter =
        factory.construct<IBasePresenter>(componentId)
            .also { component ->
                (component as? BasePresenter)?.let {
                    it.presenterProvider = this
                    it.modelProvider = modelProvider
                    it.platformProvider = platformProvider
                }
            }

    override fun <T : IBasePresenter> get(component: IComponent): T? =
        get(component.componentId)

    override fun remove(component: IComponent) {
        remove(component.componentId)
    }

    override fun remove(componentId: String) {
        map.remove(componentId)
    }
}