package com.olekdia.mvp.mocks

import com.olekdia.mvp.model.Model

class MockModelFake : Model(),  IMockModel {

    override val componentId: String
        get() = IMockModel.COMPONENT_ID

    override var modelMethodCalled: Int = 0
        private set

    override var onCreateCalled: Int = 0
        private set

    override var onDestroyCalled: Int = 0
        private set

    override fun modelMethod() {
        modelMethodCalled++
    }

    override fun getModelString(): String {
        return "Fake model string"
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