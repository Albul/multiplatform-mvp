package com.olekdia.mvp.model

import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvp.platform.IPlatformProvider
import com.olekdia.mvp.presenter.IPresenterProvider

class ModelProvider(
    protected val presenterProvider: IPresenterProvider,
    protected val platformProvider: IPlatformProvider,
    override var factory: IComponentFactory
) : IModelProvider {

    protected val map: MutableMap<String, in IBaseModel> = hashMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <T : IBaseModel> get(componentId: String): T? =
        run {
            map[componentId]
                ?: create(componentId).also {
                    map[componentId] = it
                    it.onCreate()
                }
        } as? T

    private fun create(componentId: String): IBaseModel =
        factory.construct<IBaseModel>(componentId)
            .also { component ->
                (component as? BaseModel)?.let {
                    it.presenterProvider = presenterProvider
                    it.modelProvider = this
                    it.platformProvider = platformProvider
                }
            }

    override fun remove(component: IBaseModel) {
        remove(component.componentId)
    }

    override fun remove(componentId: String) {
        map.remove(componentId)
    }
}