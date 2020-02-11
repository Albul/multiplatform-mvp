package com.olekdia.mvpcore.mocks

import com.olekdia.mvp.platform.PlatformComponent
import com.olekdia.mvpcore.platform.view.managers.ISnackManager
import com.olekdia.mvpcore.platform.view.managers.OnSnackbarStateChangedListener

class SnackManagerMock : PlatformComponent(),
    ISnackManager {

    override val componentId: String
        get() = ISnackManager.COMPONENT_ID

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

    private var snackCallbackListener: OnSnackbarStateChangedListener? = null

}

class SnackCallback(val listener: OnSnackbarStateChangedListener) :
    OnSnackbarStateChangedListener {
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