package com.olekdia.mvp

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
interface ILifecycleComponent : IComponent {
    fun onCreate()

    fun onDestroy()
}