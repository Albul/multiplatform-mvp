package com.olekdia.mvp.presenter

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvp.model.IModelProvider
import com.olekdia.mvp.model.ModelProvider

class PresenterProvider(
    presenterFactory: IComponentFactory,
    modelFactory: IComponentFactory
) : IPresenterProvider {

    protected val modelProvider: IModelProvider = ModelProvider(this, modelFactory)

    protected val map: MutableMap<String, in IBasePresenter> = hashMapOf()

    private var _factory: IComponentFactory = presenterFactory
    override var factory: IComponentFactory
        get() = _factory
        set(value) {
            _factory = value
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : IBasePresenter> get(componentId: String): T? =
        (map.getOrPut(
            componentId,
            { create(componentId) }
        ) as? T)

    private fun create(componentId: String): IBasePresenter =
        factory.construct<IBasePresenter>(componentId)
            .also { component ->
                (component as? BasePresenter)?.let {
                    it.presenterProvider = this
                    it.modelProvider = modelProvider
                }
                component.onCreate()
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