@file:JvmName("MvpExt")
package com.olekdia.mvpapp.common.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.olekdia.androidcommon.extensions.activity
import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpapp.MvpApplication

val Activity.mvpApplication: MvpApplication?
    get() = application as? MvpApplication

val Activity.presenterProvider: IComponentProvider<IPresenter>?
    get() = mvpApplication?.presenterProvider

val Fragment.mainApplication: MvpApplication?
    get() = activity?.application as? MvpApplication

val Fragment.presenterProvider: IComponentProvider<IPresenter>?
    get() = mainApplication?.presenterProvider

val View.mvpApplication: MvpApplication?
    get() = activity?.application as? MvpApplication

val View.presenterProvider: IComponentProvider<IPresenter>?
    get() = mvpApplication?.presenterProvider

val Context.mvpApplication: MvpApplication?
    get() = applicationContext as? MvpApplication

val Context.presenterProvider: IComponentProvider<IPresenter>?
    get() = mvpApplication?.presenterProvider