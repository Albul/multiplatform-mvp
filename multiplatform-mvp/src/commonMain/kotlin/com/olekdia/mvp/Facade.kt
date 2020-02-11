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

    val modelProvider: IComponentProvider<IModel> =
        ComponentProvider<IModel>(this, modelFactory)

    val presenterProvider: IComponentProvider<IPresenter> =
        ComponentProvider<IPresenter>(this, presenterFactory)

    val platformProvider: IComponentProvider<IPlatformComponent> =
        ComponentProvider<IPlatformComponent>(this, platformFactory)

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

    fun load(
        modelFactories: Array<Pair<String, ISingleComponentFactory<IModel>>>? = null,
        presenterFactories: Array<Pair<String, ISingleComponentFactory<IPresenter>>>? = null,
        platformFactories: Array<Pair<String, ISingleComponentFactory<IPlatformComponent>>>? = null
    ) {
        if (modelFactories != null) modelFactory.load(modelFactories)
        if (presenterFactories != null) presenterFactory.load(presenterFactories)
        if (platformFactories != null) platformFactory.load(platformFactories)
    }

    fun unload(
        modelIds: Array<String>? = null,
        presenterIds: Array<String>? = null,
        platformIds: Array<String>? = null
    ) {
        if (modelIds != null) modelFactory.unload(modelIds)
        if (presenterIds != null) presenterFactory.unload(presenterIds)
        if (platformIds != null) platformFactory.unload(platformIds)
    }
}