package com.olekdia.mvpcore.mocks

import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvpcore.presentation.managers.ISnackManager
import com.olekdia.mvpcore.presentation.managers.ITextManager
import com.olekdia.mvpcore.presentation.managers.IToastManager
import com.olekdia.mvpcore.domain.repositories.IDbRepository
import com.olekdia.mvpcore.domain.repositories.IPrefsRepository
import com.olekdia.mvpcore.domain.repositories.ITaskDbRepository
import io.mockk.mockk

class PlatformFactoryMock : ComponentFactory<IPlatformComponent>(

    mutableMapOf(
        IDbRepository.COMPONENT_ID to { mockk<IDbRepository>(relaxed = true, relaxUnitFun = true) },
        IPrefsRepository.COMPONENT_ID to { PrefsRepositoryMock() },
        IToastManager.COMPONENT_ID to { mockk<IToastManager>(relaxed = true, relaxUnitFun = true) },
        ISnackManager.COMPONENT_ID to { SnackManagerMock() },
        ITextManager.COMPONENT_ID to { mockk<ITextManager>(relaxed = true, relaxUnitFun = true) },

        ITaskDbRepository.COMPONENT_ID to { TaskDbRepositoryMock() }
    )
)