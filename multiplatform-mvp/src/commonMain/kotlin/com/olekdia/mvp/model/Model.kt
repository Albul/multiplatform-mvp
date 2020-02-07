package com.olekdia.mvp.model

import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvp.presenter.IPresenter

/**
 * Domain layer of the app.
 * Classes that inherited from this one should contain abstract business logic,
 * which is independent from particular framework
 */
abstract class Model : IModel {

    lateinit var modelProvider: IComponentProvider<IModel>
        internal set

    lateinit var presenterProvider: IComponentProvider<IPresenter>
        internal set

    lateinit var platformProvider: IComponentProvider<IPlatformComponent>
        internal set

    override fun onCreate() {
    }

    override fun onDestroy() {
        modelProvider.remove(this)
    }
}