package com.olekdia.mvp.mocks

import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvp.platform.PlatformComponent

interface IMockPlatformRepository : IPlatformComponent {
    val onCreateCalled: Int
    val onDestroyCalled: Int

    fun getMngSomething(): String

    companion object {
        const val COMPONENT_ID = "MOCK_PLATFORM_REP"
    }
}

class MockPlatformRepository() : PlatformComponent(),  IMockPlatformRepository {

    override val componentId: String
        get() = IMockPlatformManager.COMPONENT_ID

    override var onCreateCalled: Int = 0
        private set

    override var onDestroyCalled: Int = 0
        private set

    override fun getMngSomething(): String {
        return "Rep returns something"
    }

    override fun onCreate() {
        super.onCreate()
        onCreateCalled++
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyCalled++
    }
}