package com.olekdia.mvp.mocks

import com.olekdia.mvp.model.IModel
import com.olekdia.mvp.model.Model

interface IMockModel : IModel {

    val modelMethodCalled: Int
    val onCreateCalled: Int
    val onDestroyCalled: Int

    fun modelMethod()

    companion object {
        const val COMPONENT_ID = "MOCK_MODEL"
    }
}

class MockModel : Model(),  IMockModel {

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

    override fun onCreate() {
        super.onCreate()
        onCreateCalled++
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyCalled++
    }
}