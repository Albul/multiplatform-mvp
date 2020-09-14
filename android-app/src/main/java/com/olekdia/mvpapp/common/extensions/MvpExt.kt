@file:JvmName("MvpExt")
package com.olekdia.mvpapp.common.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.olekdia.androidcommon.extensions.activity
import com.olekdia.mvp.IMutableComponentProvider
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpapp.MvpApplication

val Activity.mvpApplication: MvpApplication?
    get() = application as? MvpApplication

val Activity.presenterProvider: IMutableComponentProvider<IPresenter>?
    get() = mvpApplication?.presenterProvider

val Fragment.mainApplication: MvpApplication?
    get() = activity?.application as? MvpApplication

val Fragment.presenterProvider: IMutableComponentProvider<IPresenter>?
    get() = mainApplication?.presenterProvider

val View.mvpApplication: MvpApplication?
    get() = activity?.application as? MvpApplication

val View.presenterProvider: IMutableComponentProvider<IPresenter>?
    get() = mvpApplication?.presenterProvider

val Context.mvpApplication: MvpApplication?
    get() = applicationContext as? MvpApplication

val Context.presenterProvider: IMutableComponentProvider<IPresenter>?
    get() = mvpApplication?.presenterProvider