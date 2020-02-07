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
interface IPresenter : IComponent {

    fun onCreate()

    fun onDestroy()
}