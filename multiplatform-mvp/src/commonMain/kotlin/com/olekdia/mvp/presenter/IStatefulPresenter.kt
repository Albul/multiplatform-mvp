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
interface IStatefulPresenter<S> : IBasePresenter {
    val state: S

    fun onRestoreState(state: S)
}