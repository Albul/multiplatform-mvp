package com.olekdia.mvpapp.platform.fragments

import androidx.fragment.app.Fragment
import com.olekdia.mvpapp.platform.fragments.IStatefulFragment.StackState

abstract class StatefulFragment : Fragment(),
    IStatefulFragment {

    private var stackState = StackState.ETERNITY

    override val isForeground: Boolean
        get() = stackState === StackState.FOREGROUND

    override fun onForeground() {
        stackState = StackState.FOREGROUND
    }

    override fun onBackground() {
        stackState = StackState.BACKGROUND
    }

    override fun onEternity() {
        stackState = StackState.ETERNITY
    }

    override fun isKeepFragment(): Boolean {
        return false
    }
}