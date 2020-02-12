package com.olekdia.mvpcore.presentation

import com.olekdia.mvpcore.BaseTest
import com.olekdia.mvpcore.TaskFilter
import com.olekdia.mvpcore.ViewType
import com.olekdia.mvpcore.presentation.views.IInputTaskView
import com.olekdia.mvpcore.presentation.views.IMainView
import com.olekdia.mvpcore.presentation.views.ITaskListView
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.*

class TaskListPresenterTest : BaseTest() {

    val taskListViewMock = mockk<ITaskListView>(relaxed = true, relaxUnitFun = true) {
        every { dataProvider } returns listOf()
        every { clearCompletedVisible } returns true
        every { isReady } returns true
        taskListPresenter.onAttach(this)
    }

    val mainViewMock = mockk<IMainView>(relaxed = true, relaxUnitFun = true) {
        mainViewPresenter.onAttach(this)
    }

    @Test
    fun `onChangeFilter() to identical filter - state is not changed`() {
        val stateBefore = taskModel.state
        taskListPresenter.onChangeFilter(stateBefore.filter)

        assertSame(stateBefore, taskModel.state)
        verify(exactly = 0) { taskListViewMock.changeFilter(stateBefore.filter) }
        verify(exactly = 0) { taskListViewMock.dataProvider = taskModel.state.filteredList }
    }

    @Test
    fun `onChangeFilter() - filter is applied`() {
        val stateBefore = taskModel.state
        taskListPresenter.onChangeFilter(TaskFilter.COMPLETED)
        assertEquals(TaskFilter.COMPLETED, taskModel.state.filter)

        taskListPresenter.onChangeFilter(TaskFilter.ALL)
        assertEquals(TaskFilter.ALL, taskModel.state.filter)

        assertNotSame(stateBefore, taskModel.state)
        verify { taskListViewMock.changeFilter(TaskFilter.ALL) }
        verify { taskListViewMock.dataProvider = taskModel.state.filteredList }
    }

    @Test
    fun `onToggleCompleted() non existing pos - state is not changed`() {
        val stateBefore = taskModel.state
        taskListPresenter.onToggleCompleted(Int.MAX_VALUE)

        assertSame(stateBefore, taskModel.state)
    }

    @Test
    fun `onToggleCompleted() - state is changed, isCompleted() returns opposite value`() {
        val stateBefore = taskModel.state
        val taskBefore = stateBefore.filteredList!![0]
        taskListPresenter.onToggleCompleted(0)

        assertNotEquals(stateBefore, taskModel.state)
        assertEquals(taskBefore.isCompleted, !taskModel.state.filteredList!![0].isCompleted)

        verify { taskListViewMock.dataProvider = taskModel.state.filteredList }
    }

    @Test
    fun `onEdit() non existing pos - state is not changed`() {
        val stateBefore = taskModel.state
        taskListPresenter.onEdit(Int.MAX_VALUE)

        assertSame(stateBefore, taskModel.state)
        verify(exactly = 0) { mainViewMock.showView(any(), any(), any()) }
    }

    @Test
    fun `onEdit() - showView() is called`() {
        val targetEntry = taskModel.state.filteredList!![0]
        taskListPresenter.onEdit(0)

        verify(exactly = 1) {
            mainViewMock.showView(
                IInputTaskView.COMPONENT_ID,
                ViewType.FORM,
                any()
            )
        }
    }

    @Test
    fun `onDelete() non existing pos - state is not changed, snack is not shown`() {
        val stateBefore = taskModel.state
        taskListPresenter.onDelete(Int.MAX_VALUE)

        assertSame(stateBefore, taskModel.state)
        assertFalse(snackMng.isShown)
    }

    @Test
    fun `onDelete() - state is changed, snack is shown`() {
        val stateBefore = taskModel.state
        taskListPresenter.onDelete(0)

        assertNotEquals(stateBefore, taskModel.state)
        assertTrue(snackMng.isShown)
    }

    @Test
    fun `onDelete(), snack apply() - state is changed`() {
        val stateBefore = taskModel.state
        taskListPresenter.onDelete(0)
        snackMng.apply()

        assertNotEquals(stateBefore, taskModel.state)
        assertFalse(snackMng.isShown)
    }

    @Test
    fun `onDelete(), snack undo() - state is not changed`() {
        val stateBefore = taskModel.state
        taskListPresenter.onDelete(0)

        assertNotEquals(stateBefore, taskModel.state)
        snackMng.undo()

        assertEquals(stateBefore, taskModel.state)
        assertFalse(snackMng.isShown)
    }

    @Test
    fun `onDeleteCompleted() - state is changed, no more completed tasks`() {
        val stateBefore = taskModel.state
        taskListPresenter.onDeleteCompleted()

        assertNotEquals(stateBefore, taskModel.state)
        assertTrue(snackMng.isShown)
        assertTrue(taskModel.state.list!!.all { !it.isCompleted })
    }

    @Test
    fun `onDeleteCompleted(), snack apply() - state is changed, no more completed tasks`() {
        val stateBefore = taskModel.state
        taskListPresenter.onDeleteCompleted()

        assertNotEquals(stateBefore, taskModel.state)
        assertTrue(snackMng.isShown)
        snackMng.apply()

        assertNotEquals(stateBefore, taskModel.state)
        assertFalse(snackMng.isShown)
        assertTrue(taskModel.state.list!!.all { !it.isCompleted })
    }

    @Test
    fun `onDeleteCompleted(), snack undo() - state is not changed`() {
        val stateBefore = taskModel.state
        taskListPresenter.onDeleteCompleted()

        assertNotEquals(stateBefore, taskModel.state)
        assertTrue(snackMng.isShown)
        snackMng.undo()

        assertEquals(stateBefore, taskModel.state)
        assertFalse(snackMng.isShown)
    }
}