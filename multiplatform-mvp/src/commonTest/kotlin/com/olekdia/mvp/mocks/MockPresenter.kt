package com.olekdia.mvp.mocks

import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvp.presenter.Presenter

interface IMockPresenter : IPresenter {

    val presMethodCalled: Int
    val onCreateCalled: Int
    val onDestroyCalled: Int

    fun presMethod()

    fun getPresSomething(): String

    companion object {
        const val COMPONENT_ID = "MOCK_PRESENTER"
    }
}

class MockPresenter : Presenter(),  IMockPresenter {

    override val componentId: String
        get() = IMockPresenter.COMPONENT_ID

    override var presMethodCalled: Int = 0
        private set

    override var onCreateCalled: Int = 0
        private set

    override var onDestroyCalled: Int = 0
        private set

    override fun presMethod() {
        presMethodCalled++
    }

    override fun getPresSomething(): String {
        return "Presenter returns something"
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