package com.olekdia.mvpcore.platform.views

import com.olekdia.mvp.IComponent

interface IMainView : IComponent {
    fun <T> getPlatformView(): T

    fun showView(componentId: String, vararg params: Pair<String, Any?>)

    companion object {
        const val COMPONENT_ID = "MAIN_VIEW_VIEW"
    }
}