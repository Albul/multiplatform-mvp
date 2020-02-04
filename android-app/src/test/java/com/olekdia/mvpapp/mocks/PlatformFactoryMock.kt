package com.olekdia.mvpapp.mocks

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvpapp.model.repositories.ITaskDbRepository
import com.olekdia.mvpapp.model.repositories.db.IDbRepository
import com.olekdia.mvpapp.presentation.presenters.ISnackManager
import org.mockito.Mockito

class PlatformFactoryMock : IComponentFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : IComponent> construct(componentId: String): T =
        when (componentId) {
            ISnackManager.COMPONENT_ID -> SnackManagerMock()
            ITaskDbRepository.COMPONENT_ID -> Mockito.mock(ITaskDbRepository::class.java)
            IDbRepository.COMPONENT_ID -> Mockito.mock(IDbRepository::class.java)
            else -> throw RuntimeException("Model class not found")
        }.let { it as T }
}