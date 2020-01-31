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
interface IViewPresenter<V, S> : IStatefulPresenter<S> {
    val view: V?

    /**
     * Called when view is crated and all view properties have been assigned.
     * In a Fragment it is called in onViewCreated
     */
    fun onAttach(v: V)

    /**
     * Called when view is going to active state.
     * Before this view is attached, state is restored, and everything is ready for interaction.
     * In this callback view should be updated, and should be in sync with presenter state.
     * In a Fragment it is called in onStart
     */
    fun onStart()

    /**
     * Called when view is going to inactive state.
     * In a Fragment it is called in onStop
     */
    fun onStop()

    /**
     * Called when view is going to be destroyed.
     * In a Fragment it is called in onDestroyView
     */
    fun onDetach(v: V)
}