package com.olekdia.mvp.model

import com.olekdia.mvp.IMutableComponentProvider
import com.olekdia.mvp.platform.IPlatformHolder
import com.olekdia.mvp.platform.IPlatformComponent

/**
 * Domain layer of the app.
 * Classes that inherited from this one should contain abstract business logic,
 * which is independent from particular framework
 */
abstract class Model : IModel,
    IModelHolder,
    IPlatformHolder {

    override lateinit var modelProvider: IMutableComponentProvider<IModel>
        internal set

    override lateinit var platformProvider: IMutableComponentProvider<IPlatformComponent>
        internal set

    override fun onCreate() {
    }

    override fun onDestroy() {
        modelProvider.remove(this)
    }
}