package com.olekdia.mvp.model

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
interface IBaseModel : IComponent {

    fun onCreate()

    fun onDestroy()
}