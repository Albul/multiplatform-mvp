package com.olekdia.mvpcore.platform.view.views

import com.olekdia.mvp.IComponent

interface IMainView : IComponent {
    fun <T> getPlatformView(): T

    fun showView(componentId: String, params: Array<Pair<String, Any?>>)

    companion object {
        const val COMPONENT_ID = "MAIN_VIEW_VIEW"
    }
}