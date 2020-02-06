@file:JvmName("MvpExt")
package com.olekdia.mvp.platform

import kotlin.jvm.JvmName
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import com.olekdia.mvp.presenter.IPresenterProvider

val Context.activity: Activity?
    get() {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

val View.activity: Activity?
    get() = context.activity

val Activity.mvpApplication: MvpApplication?
    get() = application as? MvpApplication

val Activity.presenterProvider: IPresenterProvider?
    get() = mvpApplication?.presenterProvider

val View.mvpApplication: MvpApplication?
    get() = activity?.application as? MvpApplication

val View.presenterProvider: IPresenterProvider?
    get() = mvpApplication?.presenterProvider

val Context.mvpApplication: MvpApplication?
    get() = applicationContext as? MvpApplication

val Context.presenterProvider: IPresenterProvider?
    get() = mvpApplication?.presenterProvider