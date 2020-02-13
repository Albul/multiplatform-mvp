package com.olekdia.mvp.factories

import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.mocks.IMockPlatformManager
import com.olekdia.mvp.mocks.MockPlatformManager
import com.olekdia.mvp.platform.IPlatformComponent

class PlatformMockFactory : ComponentFactory<IPlatformComponent>(

    mutableMapOf(
        IMockPlatformManager.COMPONENT_ID to { MockPlatformManager() }
    )
)