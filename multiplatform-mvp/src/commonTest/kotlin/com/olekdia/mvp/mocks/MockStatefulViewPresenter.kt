package com.olekdia.mvp.mocks

import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvp.presenter.IStatefulViewPresenter
import com.olekdia.mvp.presenter.StatefulViewPresenter

data class MockState(var stateInt: Int = 0)

interface IMockView : IComponent {
    val viewMethodCalled: Int
    fun viewMethod()
    fun attach()
    fun detach()
}

class MockView(private val presenterProvider: IComponentProvider<IPresenter>) : IMockView {
    override var viewMethodCalled: Int = 0
        private set

    override fun viewMethod() {
        viewMethodCalled++
    }

    override fun attach() {
        presenterProvider
            .get<IMockStatefulViewPresenter>(IMockStatefulViewPresenter.COMPONENT_ID)!!
            .onAttach(this)
    }

    override fun detach() {
        presenterProvider
            .get<IMockStatefulViewPresenter>(IMockStatefulViewPresenter.COMPONENT_ID)!!
            .onDetach(this)
    }

    override val componentId: String
        get() = "MOCK_VIEW"
}


interface IMockStatefulViewPresenter : IStatefulViewPresenter<IMockView, MockState> {

    val presMethodCalled: Int
    val onCreateCalled: Int
    val onRestoreStateCalled: Int
    val onAttachCalled: Int
    val onDestroyCalled: Int

    fun presMethod()

    fun onViewMethodShouldBeCalled()

    fun getPresSomething(): String

    companion object {
        const val COMPONENT_ID = "MOCK_STATEFUL_VIEW_PRESENTER"
    }
}

class MockStatefulViewPresenter : StatefulViewPresenter<IMockView, MockState>(),
    IMockStatefulViewPresenter {

    override val componentId: String
        get() = IMockPresenter.COMPONENT_ID

    override var state: MockState = MockState(0)
        private set

    override var presMethodCalled: Int = 0
        private set

    override var onCreateCalled: Int = 0
        private set

    override var onRestoreStateCalled: Int = 0
        private set

    override var onAttachCalled: Int = 0
        private set

    override var onDestroyCalled: Int = 0
        private set

    override fun presMethod() {
        presMethodCalled++
    }

    override fun onViewMethodShouldBeCalled() {
        view?.viewMethod()
    }

    override fun getPresSomething(): String {
        return "Presenter returns something"
    }

    override fun onCreate() {
        super.onCreate()
        onCreateCalled++
    }

    override fun onAttach(v: IMockView) {
        super.onAttach(v)
        onAttachCalled++
    }

    override fun onRestoreState(state: MockState) {
        super.onRestoreState(state)
        onRestoreStateCalled++
        this.state = state
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyCalled++
    }
}