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
        platformFactories: Array<Pair<String, ISingleComponentFactory<IPlatformComponent>>>? = null
    ) {
        load(modelFactories, modelFactory, modelProvider)
        load(presenterFactories, presenterFactory, presenterProvider)
        load(platformFactories, platformFactory, platformProvider)
    }

    fun unload(
        modelIds: Array<String>? = null,
        presenterIds: Array<String>? = null,
        platformIds: Array<String>? = null
    ) {
        unload(modelIds, modelFactory, modelProvider)
        unload(presenterIds, presenterFactory, presenterProvider)
        unload(platformIds, platformFactory, platformProvider)
    }

    private fun <T : ILifecycleComponent> load(
        factories: Array<Pair<String, ISingleComponentFactory<T>>>?,
        factory: IComponentFactory<T>,
        provider: ComponentProvider<T>
    ) {
        if (factories != null) {
            factory.load(factories)

            factories
                .map { it.first }
                .toTypedArray()
                .let { keys ->
                    unloadInstances(keys, provider)
                }
        }
    }

    private fun <T : ILifecycleComponent> unload(
        ids: Array<String>?,
        factory: IComponentFactory<T>,
        provider: ComponentProvider<T>
    ) {
        if (ids != null) {
            factory.unload(ids)
            unloadInstances(ids, provider)
        }
    }

    private fun <T : ILifecycleComponent> unloadInstances(
        ids: Array<String>,
        provider: ComponentProvider<T>
    ) {
        for (id in ids) {
            provider.removeAll(id)
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