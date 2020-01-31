package com.olekdia.sample.model.models

import com.olekdia.mvp.model.IStatefulModel
import com.olekdia.mvp.model.StatefulModel
import com.olekdia.sample.ModelId
import com.olekdia.sample.model.entries.TaskEntry
import com.olekdia.sample.model.repositories.ITaskDbRepository

data class TaskListState(val taskList: List<TaskEntry>? = null)

interface ITaskModel : IStatefulModel<TaskListState> {
    val list: List<TaskEntry>?

    fun loadAsync(onComplete: () -> Unit)

    fun add(task: TaskEntry)

    fun save(task: TaskEntry)

    fun complete(task: TaskEntry)

    fun remove(task: TaskEntry)

    companion object {
        const val COMPONENT_ID = ModelId.TASK_MODEL
    }
}

class TaskModel : StatefulModel<TaskListState>(), ITaskModel {

    private val taskDbRep: ITaskDbRepository?
        get() = modelProvider.get(ITaskDbRepository.COMPONENT_ID)

    private var taskList: ArrayList<TaskEntry> = arrayListOf()

    override val list: List<TaskEntry>?
        get() = state.taskList

    override fun loadAsync(onComplete: () -> Unit) {
        taskDbRep?.loadAsync { list ->
            taskList = ArrayList(list)
            state = state.copy(taskList = list)
            onComplete()
        }
    }

    override fun add(task: TaskEntry) {
        taskList.add(task)
        state = state.copy(taskList)

        taskDbRep?.add(task, taskList.lastIndex)
    }

    override fun save(task: TaskEntry) {
        state = state.copy(
            taskList = state.taskList?.replace(task) { it.id == task.id }
        )

        taskDbRep?.save(task)
    }

    override fun remove(task: TaskEntry) {
        taskList.remove(task)
        state = state.copy(taskList)

        taskDbRep?.remove(task)
    }

    override fun complete(task: TaskEntry) {
        task.isCompleted = !task.isCompleted
        state = state.copy(taskList)

        taskDbRep?.save(task)
    }

//--------------------------------------------------------------------------------------------------
//  Model lifecycle
//--------------------------------------------------------------------------------------------------

    override val componentId: String
        get() = ITaskModel.COMPONENT_ID

    override var state: TaskListState = TaskListState()
}

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
    return map {
        if (block(it)) newValue else it
    }
}
