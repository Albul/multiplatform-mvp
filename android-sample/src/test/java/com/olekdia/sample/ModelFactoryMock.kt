package com.olekdia.sample.presenter

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.sample.model.models.ITaskModel
import com.olekdia.sample.model.models.TaskModel
import com.olekdia.sample.model.repositories.ITaskDbRepository
import com.olekdia.sample.model.repositories.db.IDbRepository
import org.mockito.Mockito

class ModelFactoryMock : IComponentFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : IComponent> construct(componentId: String): T =
        when (componentId) {
            ITaskModel.COMPONENT_ID -> TaskModel()
            ITaskDbRepository.COMPONENT_ID -> Mockito.mock(ITaskDbRepository::class.java)
            IDbRepository.COMPONENT_ID -> Mockito.mock(IDbRepository::class.java)
            else -> throw RuntimeException("Model class not found")
        }.let { it as T }
}