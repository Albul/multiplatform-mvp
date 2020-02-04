package com.olekdia.mvpcore.platform.managers

import com.olekdia.mvp.platform.IBasePlatformComponent

interface OnSnackbarStateChangedListener {
    fun onUndo()
    fun onApply()
}

interface ISnackManager : IBasePlatformComponent {

    val isShown: Boolean

    fun show(
        content: CharSequence,
        action: CharSequence,
        listener: OnSnackbarStateChangedListener,
        inFormView: Boolean = true
    )

    fun undo()

    fun apply()

    companion object {
        const val COMPONENT_ID: String = "SNACK_MNG"
    }
}