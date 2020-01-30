package com.olekdia.sample.view.fragments

interface IStatefulFragment {

    enum class StackState {
        FOREGROUND, BACKGROUND, ETERNITY
    }

    val isForeground: Boolean

    fun onForeground()

    fun onBackground()

    fun onEternity()

    /**
     * This call back is called when back button in activity is pressed
     * @return true keep the fragment, prevent of removing it from back stack, false - allow to remove it
     */
    fun onBackPressed(): Boolean
}