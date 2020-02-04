package com.olekdia.mvp.model

import com.olekdia.mvp.platform.IPlatformProvider
import com.olekdia.mvp.presenter.IPresenterProvider

/**
 * Domain layer of the app.
 * Classes that inherited from this one should contain abstract business logic,
 * which is independent from particular framework
 */
abstract class BaseModel : IBaseModel {

    lateinit var presenterProvider: IPresenterProvider
        internal set

    lateinit var modelProvider: IModelProvider
        internal set

    lateinit var platformProvider: IPlatformProvider
        internal set

    override fun onCreate() {
    }

    override fun onDestroy() {
        modelProvider.remove(this)
    }
}