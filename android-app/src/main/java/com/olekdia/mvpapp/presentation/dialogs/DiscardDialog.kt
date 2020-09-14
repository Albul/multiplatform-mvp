package com.olekdia.mvpapp.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.olekdia.materialdialog.MaterialDialog
import com.olekdia.materialdialog.MaterialDialogBuilder
import com.olekdia.mvpapp.R
import com.olekdia.mvpapp.common.extensions.presenterProvider
import com.olekdia.mvpcore.Key
import com.olekdia.mvpcore.Param
import com.olekdia.mvpcore.presentation.views.IDiscardDialogView
import com.olekdia.mvpcore.presentation.presenters.IDialogPresenter

class DiscardDialog : DialogFragment(), IDiscardDialogView {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: MaterialDialog = initBuilder().build()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return dialog
    }

    private fun initBuilder(): MaterialDialogBuilder {
        val args = arguments
        val id = args!!.getString(Key.ID)!!
        return MaterialDialogBuilder(requireContext())
            .title(R.string.discard_changes)
            .content(args.getString(Key.CONTENT, ""))
            .positiveText(R.string.discard)
            .negativeText(R.string.cancel)
            .neutralText(R.string.save)
            .callback(object : MaterialDialog.ButtonCallback() {
                override fun onPositive(dialog: MaterialDialog?) {
                    apply(id, Param.DISCARD)
                }

                override fun onNeutral(dialog: MaterialDialog?) {
                    apply(id, Param.SAVE)
                }
            })
    }

    override fun apply(componentId: String, @Param param: String) {
        presenterProvider
            ?.getOrCreate<IDialogPresenter>(IDialogPresenter.COMPONENT_ID)
            ?.onDiscardDlg(componentId, param)
    }
}