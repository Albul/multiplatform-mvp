package com.olekdia.mvpapp.presentation

import com.olekdia.mvpapp.BaseTest
import com.olekdia.mvpcore.model.models.ITaskModel
import com.olekdia.mvpcore.presentation.presenters.ITaskListPresenter
import com.olekdia.mvpcore.presentation.presenters.TaskListPresenter
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TaskListPresenterTest : BaseTest() {

    val listPresenter: TaskListPresenter = presenterProvider.get(ITaskListPresenter.COMPONENT_ID)!!
    val taskModel: ITaskModel = modelProvider.get(ITaskModel.COMPONENT_ID)!!

    @Test
    fun presWithoutChange_isStateUnsaved_false() {
        listPresenter.onDelete(2)
    }
}