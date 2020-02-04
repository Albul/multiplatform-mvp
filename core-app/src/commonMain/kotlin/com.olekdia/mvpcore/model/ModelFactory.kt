package com.olekdia.mvpcore.model

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvpcore.model.models.ITaskModel
import com.olekdia.mvpcore.model.models.TaskModel

class ModelFactory() : IComponentFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : IComponent> construct(componentId: String): T =
        when (componentId) {
            ITaskModel.COMPONENT_ID -> TaskModel()
            else -> throw RuntimeException("Model class not found")
        }.let { it as T }

}