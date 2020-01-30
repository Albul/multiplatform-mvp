package com.olekdia.sample.model

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.sample.ModelId
import com.olekdia.sample.model.models.TaskModel
import com.olekdia.sample.model.repositories.db.DbRepository
import com.olekdia.sample.model.repositories.TaskDbRepository

class ModelFactory : IComponentFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : IComponent> construct(componentId: String): T =
        when (componentId) {
            ModelId.TASK -> TaskModel()
            ModelId.TASK_DB_REPOSITORY -> TaskDbRepository()
            ModelId.DB_REPOSITORY -> DbRepository()
            else -> throw RuntimeException("Model class not found")
        }.let { it as T }

}