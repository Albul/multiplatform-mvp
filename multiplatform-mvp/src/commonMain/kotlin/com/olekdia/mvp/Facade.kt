package com.olekdia.mvp

import com.olekdia.mvp.model.Model
import com.olekdia.mvp.model.IModel
import com.olekdia.mvp.platform.PlatformComponent
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvp.presenter.Presenter
import com.olekdia.mvp.presenter.IPresenter

class Facade(
    private val modelFactory: IComponentFactory<IModel>,
    private val presenterFactory: IComponentFactory<IPresenter>,
    private val platformFactory: IComponentFactory<IPlatformComponent>
) {

    val modelProvider: ComponentProvider<IModel> =
        ComponentProvider<IModel>(this, modelFactory)

    val presenterProvider: ComponentProvider<IPresenter> =
        ComponentProvider<IPresenter>(this, presenterFactory)

    val platformProvider: ComponentProvider<IPlatformComponent> =
        ComponentProvider<IPlatformComponent>(this, platformFactory)

    fun load(
        modelFactories: Array<Pair<String, ISingleComponentFactory<IModel>>>? = null,
        presenterFactories: Array<Pair<String, ISingleComponentFactory<IPresenter>>>? = null,
        platformFactories: Array<Pair<String, ISingleComponentFactory<IPlatformComponent>>>? = null,
        reloadInstances: Boolean = true
    ) {
        load(modelFactories, modelFactory, modelProvider, reloadInstances)
        load(presenterFactories, presenterFactory, presenterProvider, reloadInstances)
        load(platformFactories, platformFactory, platformProvider, reloadInstances)
    }

    fun unload(
        modelIds: Array<String>? = null,
        presenterIds: Array<String>? = null,
        platformIds: Array<String>? = null,
        unloadInstances: Boolean = true
    ) {
        unload(modelIds, modelFactory, modelProvider, unloadInstances)
        unload(presenterIds, presenterFactory, presenterProvider, unloadInstances)
        unload(platformIds, platformFactory, platformProvider, unloadInstances)
    }

    private fun <T : ILifecycleComponent> load(
        factories: Array<Pair<String, ISingleComponentFactory<T>>>?,
        factory: IComponentFactory<T>,
        provider: ComponentProvider<T>,
        reloadInstances: Boolean
    ) {
        if (factories != null) {
            factory.load(factories)
            if (reloadInstances) {
                factories
                    .map { it.first }
                    .toTypedArray()
                    .let { keys ->
                        unload(keys, provider)
                        load(keys, provider)
                    }
            }
        }
    }

    private fun <T : ILifecycleComponent> unload(
        ids: Array<String>?,
        factory: IComponentFactory<T>,
        provider: ComponentProvider<T>,
        reloadInstances: Boolean
    ) {
        if (ids != null) {
            factory.unload(ids)
            if (reloadInstances) {
                unload(ids, provider)
            }
        }
    }

    private fun <T : ILifecycleComponent> unload(
        ids: Array<String>,
        provider: ComponentProvider<T>
    ) {
        for (id in ids) {
            provider.remove(id)
        }
    }

    private fun <T : ILifecycleComponent> load(
        ids: Array<String>,
        provider: ComponentProvider<T>
    ) {
        for (id in ids) {
            provider.get<T>(id)
        }
    }

    internal fun inject(component: ILifecycleComponent) {
        when (component) {
            is Presenter -> {
                component.modelProvider = modelProvider
                component.presenterProvider = presenterProvider
                component.platformProvider = platformProvider
            }
            is Model -> {
                component.modelProvider = modelProvider
                component.platformProvider = platformProvider
            }
            is PlatformComponent -> {
                component.presenterProvider = presenterProvider
                component.platformProvider = platformProvider
            }
        }
    }
}