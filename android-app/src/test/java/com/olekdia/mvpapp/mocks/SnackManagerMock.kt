package com.olekdia.mvpapp.mocks

import com.olekdia.mvp.platform.BasePlatformComponent
import com.olekdia.mvpapp.presentation.presenters.ISnackManager
import com.olekdia.mvpapp.presentation.presenters.OnSnackbarStateChangedListener

class SnackManagerMock : BasePlatformComponent(), ISnackManager {

    private var snackCallbackListener: OnSnackbarStateChangedListener? = null

    override val isShown: Boolean
        get() = snackCallbackListener != null

    override fun show(
        content: CharSequence,
        action: CharSequence,
        listener: OnSnackbarStateChangedListener,
        fromFormView: Boolean
    ) {
        apply()
        snackCallbackListener = SnackCallback(listener)
    }

    override fun undo() {
        snackCallbackListener?.onUndo()
        snackCallbackListener = null
    }

    override fun apply() {
        snackCallbackListener?.onApply()
        snackCallbackListener = null
    }
}

class SnackCallback(val listener: OnSnackbarStateChangedListener) : OnSnackbarStateChangedListener {
    private var fired = false

    override fun onUndo() {
        if (!fired) listener.onUndo()
        fired = true
    }

    override fun onApply() {
        if (!fired) listener.onApply()
        fired = true
    }
}