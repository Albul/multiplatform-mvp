package com.olekdia.mvp.presenter

import com.olekdia.mvp.IComponent

/**
 *           Lifecycle:
 *          ------------
 *         | onCreate() |
 *          ------------
 *               ↓
 *          -------------
 *         | onDestroy() |
 *          -------------
 */
interface IBasePresenter : IComponent {

    fun onCreate()

    fun onDestroy()
}