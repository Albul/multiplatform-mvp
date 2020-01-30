package com.olekdia.mvp.model

import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvp.presenter.IPresenterProvider

abstract class BaseModelProvider(
    protected val presenterProvider: IPresenterProvider,
    override var factory: IComponentFactory
) : IModelProvider {

    protected val map: MutableMap<String, in IBaseModel> = hashMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <T : IBaseModel> get(componentId: String): T? =
        (map.getOrPut(
            componentId,
            { create(componentId) }
        ) as? T)

    private fun create(componentId: String): IBaseModel =
        factory.construct<IBaseModel>(componentId)
            .also { component ->
                (component as? BaseModel)?.let {
                    it.presenterProvider = presenterProvider
                    it.modelProvider = this
                }
                component.configure()
                component.onCreate()
            }

    abstract fun IBaseModel.configure()

    override fun remove(component: IBaseModel) {
        remove(component.componentId)
    }

    override fun remove(componentId: String) {
        map.remove(componentId)
    }
}