package com.olekdia.mvp.presenter

/**
 *           Lifecycle:
 *          ------------
 *         | onCreate() |
 *          ------------
 *               ↓
 *          ------------
 *         | onAttach() |
 *          ------------
 *               ↓
 *       ------------------
 *      | onRestoreState() |
 *       ------------------
 *               ↓
 *          -----------
 *         | onStart() |
 *          -----------
 *               ↓
 *           ----------
 *          | onStop() |
 *           ----------
 *               ↓
 *          ------------
 *         | onDetach() |
 *          ------------
 *               ↓
 *          -------------
 *         | onDestroy() |
 *          -------------
 */
interface IStatefulViewPresenter<V, S> : IViewPresenter<V>, IStatefulPresenter<S>