package com.olekdia.mvp.presenter

import android.content.Context
import android.os.Bundle

open class PlatformPresenter : IPlatformPresenter {

    override var context: Context? = null

    override fun onSaveInstanceState(outState: Bundle?) {
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    }
}