package com.olekdia.mvpcore.presentation.views

import com.olekdia.mvp.IComponent
import com.olekdia.mvpcore.ViewType

interface IMainView : IComponent {
    fun <T> getPlatformView(): T

    /**
     * @return true if view is shown
     */
    fun showView(
        componentId: String,
        viewType: ViewType,
        params: Array<Pair<String, Any?>>
    ): Boolean

    companion object {
        const val COMPONENT_ID = "MAIN_VIEW_VIEW"
    }
}