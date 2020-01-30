package com.olekdia.sample.presenter

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.sample.PresenterId

class PresenterFactory : IComponentFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : IComponent> construct(componentId: String): T =
        when (componentId) {
            PresenterId.INPUT_TASK_PRESENTER -> InputTaskPresenter()
            PresenterId.TASK_LIST_PRESENTER -> TaskListPresenter()
            else -> throw RuntimeException("Presenter class not found")
        }.let { it as T }

}