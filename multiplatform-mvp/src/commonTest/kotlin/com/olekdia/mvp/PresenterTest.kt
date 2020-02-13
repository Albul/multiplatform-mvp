package com.olekdia.mvp

import com.olekdia.mvp.mocks.*
import kotlin.test.*

class PresenterTest : BaseTest() {

    @Test
    fun `onRestoreState() - state is changed`() {
        val newState = MockState(5)
        val presenter: IMockStatefulViewPresenter = presenterProvider
            .get(IMockStatefulViewPresenter.COMPONENT_ID)!!
        presenter.onRestoreState(newState)
        assertSame(newState, presenter.state)
        assertEquals(5, presenter.state.stateInt)
        assertEquals(1, presenter.onRestoreStateCalled)
    }

    @Test
    fun `attach() on view - view is attached`() {
        val mockView = MockView(presenterProvider)
        mockView.attach()

        val presenter: IMockStatefulViewPresenter = presenterProvider
            .get(IMockStatefulViewPresenter.COMPONENT_ID)!!
        assertSame(mockView, presenter.view)
        assertEquals(1, presenter.onAttachCalled)
    }

    @Test
    fun `detach() on view - view is detached`() {
        val mockView = MockView(presenterProvider)
        mockView.attach()
        mockView.detach()

        val presenter: IMockStatefulViewPresenter = presenterProvider
            .get(IMockStatefulViewPresenter.COMPONENT_ID)!!
        assertNull(presenter.view)
        assertEquals(1, presenter.onAttachCalled)
    }

    @Test
    fun `onDetach() different view - view is not detached`() {
        val mockView = MockView(presenterProvider)
        mockView.attach()

        val presenter: IMockStatefulViewPresenter = presenterProvider
            .get(IMockStatefulViewPresenter.COMPONENT_ID)!!

        presenter.onDetach(MockView(presenterProvider))
        assertSame(mockView, presenter.view)
    }

    @Test
    fun `Call method on presenter - view method is called`() {
        val mockView = MockView(presenterProvider)
        mockView.attach()

        val presenter: IMockStatefulViewPresenter = presenterProvider
            .get(IMockStatefulViewPresenter.COMPONENT_ID)!!

        assertEquals(0, mockView.viewMethodCalled)
        presenter.onViewMethodShouldBeCalled()
        assertEquals(1, mockView.viewMethodCalled)
    }

    @Test
    fun `Call method on presenter - model method is called`() {
        val presenter: IMockPresenter = retrieveMockPresenter()

        assertEquals(0, retrieveMockModel().modelMethodCalled)
        presenter.onModelMethodShouldBeCalled()
        assertEquals(1, retrieveMockModel().modelMethodCalled)
    }
}