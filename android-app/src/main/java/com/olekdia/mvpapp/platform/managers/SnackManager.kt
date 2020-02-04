package com.olekdia.mvpapp.platform.managers

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.olekdia.common.WeakReference
import com.olekdia.mvp.platform.BasePlatformComponent
import com.olekdia.mvpapp.MainActivity
import com.olekdia.mvpapp.R
import com.olekdia.mvpcore.platform.managers.ISnackManager
import com.olekdia.mvpcore.platform.managers.OnSnackbarStateChangedListener
import com.olekdia.mvpcore.presentation.presenters.IMainViewPresenter

class SnackManager : BasePlatformComponent(),
    ISnackManager {

    override val componentId: String
        get() = ISnackManager.COMPONENT_ID

    override val isShown: Boolean
        get() = snack?.isShown ?: false

    override fun show(
        content: CharSequence,
        action: CharSequence,
        listener: OnSnackbarStateChangedListener,
        inFormView: Boolean
    ) {
        apply()

        if (inFormView) {
            mainActivity?.formContainer
        } else {
            mainActivity?.snackbarContainer
        }?.let { container ->
            snackRef = WeakReference(
                Snackbar.make(container, content, Snackbar.LENGTH_SHORT)
                    .also { snack ->
                        snack.view.tag = listener
                        snack.setAction(
                            action,
                            {
                                (snack.view.tag as? OnSnackbarStateChangedListener)?.onUndo()
                                snack.view.tag = null
                            }
                        )
                        snack.addCallback(SnackCallback().also { snackCallbackListener = it })
                        snack.customize(container)
                        snack.show()
                    }
            )
        }
    }

    override fun undo() {
        snack?.run {
            (view.tag as? OnSnackbarStateChangedListener)?.onUndo()
            cancel()
        }
    }

    override fun apply() {
        snack?.run {
            (view.tag as? OnSnackbarStateChangedListener)?.onApply()
            cancel()
        }
    }

//--------------------------------------------------------------------------------------------------
//  Details
//--------------------------------------------------------------------------------------------------

    private val mainActivity: MainActivity?
        get() = presenterProvider
            .get<IMainViewPresenter>(IMainViewPresenter.COMPONENT_ID)
            ?.view
            ?.getPlatformView()

    private var snackRef: WeakReference<Snackbar>? = null
    private val snack: Snackbar?
        get() = snackRef?.get()

    private var snackCallbackListener: SnackCallback? = null

    private fun Snackbar.cancel() {
        view.tag = null
        snackCallbackListener?.let {
            removeCallback(it)
            snackCallbackListener = null
        }

        setAction("", null)
        dismiss()
        snackRef = null
    }

    protected fun Snackbar.customize(container: View) {
        mainActivity?.let { act ->
            if (container == act.snackbarContainer) {
                if (act.isFabShown) {
                    this.setAnchorView(R.id.fab_expand_menu_button)
                }
            }
        }
    }
}

class SnackCallback : Snackbar.Callback() {
    private var fired = false

    override fun onDismissed(snackbar: Snackbar?, event: Int) { // Apply action forever
        if (event != DISMISS_EVENT_ACTION
            && !fired
        ) {
            (snackbar?.view?.tag as? OnSnackbarStateChangedListener)?.onApply()
            fired = true
        }
    }
}