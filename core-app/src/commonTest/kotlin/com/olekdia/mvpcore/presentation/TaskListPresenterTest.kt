package com.olekdia.mvpcore.presentation

import com.olekdia.mvpcore.BaseTest
import com.olekdia.mvpcore.model.models.ITaskModel
import com.olekdia.mvpcore.presentation.presenters.ITaskListPresenter
import com.olekdia.mvpcore.presentation.presenters.TaskListPresenter
import kotlin.test.Test

class TaskListPresenterTest : BaseTest() {

    val listPresenter: TaskListPresenter = presenterProvider.get(ITaskListPresenter.COMPONENT_ID)!!
    val taskModel: ITaskModel = modelProvider.get(ITaskModel.COMPONENT_ID)!!

    @Test
    fun presWithoutChange_isStateUnsaved_false() {
        listPresenter.onDelete(2)
    }
}