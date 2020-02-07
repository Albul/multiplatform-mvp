@file:JvmName("ViewExt")
package com.olekdia.mvpapp.platform.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.olekdia.androidcommon.extensions.activity
import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpapp.MainApplication

val Activity.mainApplication: MainApplication?
    get() = application as? MainApplication

val Activity.presenterProvider: IComponentProvider<IPresenter>?
    get() = mainApplication?.presenterProvider

val View.mainApplication: MainApplication?
    get() = activity?.application as? MainApplication

val Fragment.mainApplication: MainApplication?
    get() = activity?.application as? MainApplication

val Fragment.presenterProvider: IComponentProvider<IPresenter>?
    get() = mainApplication?.presenterProvider

val View.presenterProvider: IComponentProvider<IPresenter>?
    get() = mainApplication?.presenterProvider

val Context.mainApplication: MainApplication?
    get() = applicationContext as? MainApplication

val Context.presenterProvider: IComponentProvider<IPresenter>?
    get() = mainApplication?.presenterProvider