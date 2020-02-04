package com.olekdia.mvpapp.platform.managers

import android.content.Context
import android.widget.Toast
import com.olekdia.common.WeakReference
import com.olekdia.mvp.platform.BasePlatformComponent
import com.olekdia.mvpcore.platform.managers.IToastManager

class ToastManager(private val context: Context) : BasePlatformComponent(),
    IToastManager {

    private var toastRef: WeakReference<Toast>? = null
    private val toast: Toast?
        get() = toastRef?.get()

    override fun show(text: CharSequence) {
        toastRef = WeakReference(
            Toast.makeText(context, text, Toast.LENGTH_SHORT)
                .also {
                    //it.customize(TOAST_BG_ALPHA) todo
                    it.show()
                }
        )
    }

    override fun hide() {
        toast?.cancel()
        toastRef = null
    }

    override val componentId: String
        get() = IToastManager.COMPONENT_ID
}