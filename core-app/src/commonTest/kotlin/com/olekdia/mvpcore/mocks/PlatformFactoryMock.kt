package com.olekdia.mvpcore.mocks

import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvpcore.platform.managers.ISnackManager
import com.olekdia.mvpcore.platform.managers.ITextManager
import com.olekdia.mvpcore.platform.managers.IToastManager
import com.olekdia.mvpcore.platform.repositories.IDbRepository
import com.olekdia.mvpcore.platform.repositories.IPrefRepository
import com.olekdia.mvpcore.platform.repositories.ITaskDbRepository
import io.mockk.mockk

class PlatformFactoryMock : ComponentFactory<IPlatformComponent>(

    mutableMapOf(
        IDbRepository.COMPONENT_ID to { mockk<IDbRepository>(relaxed = true, relaxUnitFun = true) },
        IPrefRepository.COMPONENT_ID to { PrefRepositoryMock() },
        IToastManager.COMPONENT_ID to { mockk<IToastManager>(relaxed = true, relaxUnitFun = true) },
        ISnackManager.COMPONENT_ID to { SnackManagerMock() },
        ITextManager.COMPONENT_ID to { mockk<ITextManager>(relaxed = true, relaxUnitFun = true) },

        ITaskDbRepository.COMPONENT_ID to { TaskDbRepositoryMock() }
    )
)