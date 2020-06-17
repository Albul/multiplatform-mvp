package com.olekdia.mvp.mocks

import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvp.platform.PlatformComponent

interface IMockPlatformManager : IPlatformComponent {
    val mngMethodCalled: Int
    val onCreateCalled: Int
    val onDestroyCalled: Int

    fun mngMethod()

    fun getMngSomething(): String

    companion object {
        const val COMPONENT_ID = "MOCK_PLATFORM_MNG"
    }
}

class MockPlatformManager(val param: String) : PlatformComponent(),  IMockPlatformManager {

    override val componentId: String
        get() = IMockPlatformManager.COMPONENT_ID

    override var mngMethodCalled: Int = 0
        private set

    override var onCreateCalled: Int = 0
        private set

    override var onDestroyCalled: Int = 0
        private set

    override fun mngMethod() {
        mngMethodCalled++
    }

    override fun getMngSomething(): String {
        return "Mng returns something"
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