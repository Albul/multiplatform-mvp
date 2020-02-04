package com.olekdia.mvp.platform

import com.olekdia.mvp.presenter.IPresenterProvider

/**
 * This is a base class for framework component.
 * This class is framework dependent, it could take some platform objects as a constructor parameters.
 * Examples of classes that could be inherited from this one are:
 *  - Repositories that need to have specific context
 *  - Managers
 *  - Services
 */
abstract class BasePlatformComponent : IBasePlatformComponent {

    lateinit var presenterProvider: IPresenterProvider
        internal set

    lateinit var platformProvider: IPlatformProvider
        internal set

    override fun onCreate() {
    }

    override fun onDestroy() {
        platformProvider.remove(this)
    }
}