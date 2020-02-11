package com.olekdia.mvpcore.platform.view.views

import com.olekdia.mvpcore.Param

interface IDiscardDialogView {

    fun apply(componentId: String, @Param param: String)

    companion object {
        const val COMPONENT_ID: String = "DISCARD_DLG"
    }
}