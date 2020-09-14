package com.olekdia.mvp.platform

import com.olekdia.mvp.IMutableComponentProvider
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvp.presenter.IPresenterHolder

/**
 * This is a base class for framework component.
 * This class is framework dependent, it could take some platform objects as a constructor parameters.
 * Examples of classes that could be inherited from this one are:
 *  - Repositories that need to have specific context
 *  - Managers
 *  - Services
 */
abstract class PlatformComponent : IPlatformComponent,
    IPresenterHolder,
    IPlatformHolder {

    override lateinit var presenterProvider: IMutableComponentProvider<IPresenter>
        internal set

    override lateinit var platformProvider: IMutableComponentProvider<IPlatformComponent>
        internal set

    override fun onCreate() {
    }

    override fun onDestroy() {
        platformProvider.remove(this)
    }
}