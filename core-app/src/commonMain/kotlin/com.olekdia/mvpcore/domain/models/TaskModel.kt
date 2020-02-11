package com.olekdia.mvpcore.domain.models

import com.olekdia.mvp.model.IStatefulModel
import com.olekdia.mvp.model.StatefulModel
import com.olekdia.mvpcore.TaskFilter
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpcore.platform.extensions.plus
import com.olekdia.mvpcore.platform.extensions.replace
import com.olekdia.mvpcore.platform.view.managers.PrefManager
import com.olekdia.mvpcore.platform.data.repositories.ITaskDbRepository

data class TaskListState(
    val filter: TaskFilter = PrefManager.taskFilter.getEnumValue(),
    val list: List<TaskEntry>? = null // Null indicates that tasks not loaded yet
) {
    val filteredList: List<TaskEntry>? = list?.filterList(filter)

    /**
     * @return true if full list has at least one completed task
     */
    fun hasCompleted(): Boolean {
        if (list != null) {
            for (entry in list) {
                if (entry.isCompleted) return true
            }
        }

        return false
    }

    fun getById(id: Long): TaskEntry? {
        if (list != null) {
            for (entry in list) {
                if (entry.id == id) return entry
            }
        }
        return null
    }
}

interface ITaskModel : IStatefulModel<TaskListState> {

    /**
     * Load asynchronously tasks
     */
    fun loadAsync(onComplete: () -> Unit)

    /**
     * Insert task to repository and add to state
     */
    fun insertNew(newEntry: TaskEntry)

    /**
     * Add task to state
     */
    fun add(entry: TaskEntry)
    /**
     * Add task to state, to specific position
     */
    fun add(entry: TaskEntry, pos: Int)

    fun save(updatedEntry: TaskEntry): Boolean

    /**
     * Change isCompleted parameter of the task
     */
    fun toggleComplete(entry: TaskEntry)

    fun saveFilter(newFilter: TaskFilter): Boolean

    /**
     * Remove task from state, but not from repository
     */
    fun remove(entry: TaskEntry)
    /**
     * Remove tasks from state, but not from repository
     * @return list of removed tasks, or null if non tasks removed
     */
    fun removeCompleted(): List<TaskEntry>?
    /**
     * Put back list to the state
     */
    fun restore(list: List<TaskEntry>)
    /**
     * Delete task from repository, call remove before it
     */
    fun delete(entry: TaskEntry)
    /**
     * Delete tasks from repository, call remove before it
     */
    fun delete(list: List<TaskEntry>)

    companion object {
        const val COMPONENT_ID = "TASK_MODEL"
    }
}

class TaskModel : StatefulModel<TaskListState>(),
    ITaskModel {

    override val componentId: String
        get() = ITaskModel.COMPONENT_ID

    override fun loadAsync(onComplete: () -> Unit) {
        taskDbRep.loadAsync { list ->
            state = state.copy(list = list)
            onComplete()
        }
    }

    override fun insertNew(newEntry: TaskEntry) {
        add(
            taskDbRep.insert(newEntry, state.list?.lastIndex ?: 0)
        )
    }

    override fun add(entry: TaskEntry) {
        state = state.copy(list = state.list?.plus(entry))
    }

    override fun add(entry: TaskEntry, pos: Int) {
        state = state.copy(list = state.list?.plus(pos, entry))
    }

    override fun save(updatedEntry: TaskEntry): Boolean =
        state.getById(updatedEntry.id)
            ?.let { existedEntry ->
                if (existedEntry == updatedEntry) {
                    false
                } else {
                    state = state.copy(
                        list = state.list?.replace(updatedEntry) { it.id == updatedEntry.id }
                    )

                    taskDbRep.save(updatedEntry)

                    true
                }
            }
            ?: false

    override fun toggleComplete(entry: TaskEntry) {
        save(entry.copy(isCompleted = !entry.isCompleted))
    }

    override fun saveFilter(newFilter: TaskFilter): Boolean =
        state.list?.let { list ->
            if (state.filter == newFilter) {
                false
            } else {
                state = state.copy(
                    filter = newFilter,
                    list = list
                )
                PrefManager.taskFilter.setEnumValue(newFilter)
                true
            }
        } ?: false

    override fun remove(entry: TaskEntry) {
        state = state.copy(list = state.list?.minus(entry))
    }

    override fun removeCompleted(): List<TaskEntry>? =
        state.list?.filter { it.isCompleted }?.let { completedList ->
            if (completedList.isEmpty()) {
                null
            } else {
                state = state.copy(list = state.list?.filterNot { it.isCompleted })
                completedList
            }
        }

    override fun restore(list: List<TaskEntry>) {
        state = state.copy(list = list)
    }

    override fun delete(entry: TaskEntry) {
        taskDbRep.delete(entry)
    }

    override fun delete(list: List<TaskEntry>) {
        taskDbRep.delete(list)
    }

//--------------------------------------------------------------------------------------------------
//  Details
//--------------------------------------------------------------------------------------------------

    override var state: TaskListState =
        TaskListState()

    private val taskDbRep: ITaskDbRepository
        get() = platformProvider.get(ITaskDbRepository.COMPONENT_ID)!!
}

fun List<TaskEntry>.filterList(filter: TaskFilter) =
    when (filter) {
        TaskFilter.ALL -> this
        TaskFilter.ACTIVE -> this.filter { !it.isCompleted }
        TaskFilter.COMPLETED -> this.filter { it.isCompleted }
    }