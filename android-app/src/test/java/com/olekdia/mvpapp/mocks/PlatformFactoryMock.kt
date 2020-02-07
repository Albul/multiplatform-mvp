package com.olekdia.mvpapp.mocks

import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvpapp.model.repositories.db.IDbRepository
import com.olekdia.mvpcore.model.repositories.IPrefRepository
import com.olekdia.mvpcore.model.repositories.ITaskDbRepository
import com.olekdia.mvpcore.platform.managers.ISnackManager
import com.olekdia.mvpcore.platform.managers.ITextManager
import com.olekdia.mvpcore.platform.managers.IToastManager
import org.mockito.Mockito

class PlatformFactoryMock() :
    ComponentFactory<IPlatformComponent>(

        mutableMapOf(
            IDbRepository.COMPONENT_ID to { Mockito.mock(IDbRepository::class.java) },
            IPrefRepository.COMPONENT_ID to { Mockito.mock(IPrefRepository::class.java) },
            IToastManager.COMPONENT_ID to { Mockito.mock(IToastManager::class.java) },
            ISnackManager.COMPONENT_ID to { SnackManagerMock() },
            ITextManager.COMPONENT_ID to { Mockito.mock(ITextManager::class.java) },

            ITaskDbRepository.COMPONENT_ID to { Mockito.mock(ITaskDbRepository::class.java) }
        )
    )