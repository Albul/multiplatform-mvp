package com.olekdia.mvpcore.presentation

import com.olekdia.mvpcore.BaseTest
import com.olekdia.mvpcore.TaskFilter
import com.olekdia.mvpcore.presentation.views.ITaskListView
import io.mockk.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskListPresenterTest : BaseTest() {

    val taskListViewMock = mockk<ITaskListView>(relaxed = true, relaxUnitFun = true) {
        every { clearCompletedVisible } returns true
        every { isReady } returns true
        taskListPresenter.onAttach(this)
    }

    @Test
    fun onChangeFilter_filterIsApplied() {
        taskListPresenter.onChangeFilter(TaskFilter.COMPLETED)
        assertEquals(TaskFilter.COMPLETED, taskModel.state.filter)

        taskListPresenter.onChangeFilter(TaskFilter.ALL)
        assertEquals(TaskFilter.ALL, taskModel.state.filter)

        verify { taskListViewMock.changeFilter(TaskFilter.ALL) }
    }
}