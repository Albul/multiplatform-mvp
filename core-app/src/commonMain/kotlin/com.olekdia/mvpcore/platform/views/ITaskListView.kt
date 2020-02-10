package com.olekdia.mvpcore.platform.views

import com.olekdia.mvp.IComponent
import com.olekdia.mvpcore.TaskFilter
import com.olekdia.mvpcore.domain.entries.TaskEntry

interface ITaskListView : IComponent {

    val isReady: Boolean

    var dataProvider: List<TaskEntry>?

    var clearCompletedVisible: Boolean

    fun changeFilter(filter: TaskFilter)

    fun refresh()

    companion object {
        const val COMPONENT_ID = "TASK_LIST_VIEW"
    }
}