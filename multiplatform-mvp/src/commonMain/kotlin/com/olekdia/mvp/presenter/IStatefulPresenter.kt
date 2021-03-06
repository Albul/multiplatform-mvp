package com.olekdia.mvp.presenter

/**
 *           Lifecycle:
 *          ------------
 *         | onCreate() |
 *          ------------
 *               ↓
 *       ------------------
 *      | onRestoreState() |
 *       ------------------
 *               ↓
 *          -------------
 *         | onDestroy() |
 *          -------------
 */
interface IStatefulPresenter<S> : IPresenter {
    val state: S

    fun onRestoreState(state: S)
}