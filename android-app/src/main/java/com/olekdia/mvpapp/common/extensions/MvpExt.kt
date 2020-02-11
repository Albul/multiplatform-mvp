@file:JvmName("MvpExt")
package com.olekdia.mvpapp.common.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.olekdia.androidcommon.extensions.activity
import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvp.presenter.Presenter
import com.olekdia.mvpapp.MainApplication
import com.olekdia.mvpcore.presentation.managers.ITextManager
import com.olekdia.mvpcore.presentation.presenters.IDialogPresenter
import com.olekdia.mvpcore.presentation.presenters.IMainViewPresenter
import com.olekdia.mvpcore.presentation.presenters.ISnackPresenter
import com.olekdia.mvpcore.presentation.presenters.IToastPresenter

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