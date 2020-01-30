package com.olekdia.mvp.presenter

import android.content.Context
import android.os.Bundle

actual interface IPlatformPresenter {
    var context: Context?

    fun onSaveInstanceState(outState: Bundle?)

    fun onRestoreInstanceState(savedInstanceState: Bundle?)
}