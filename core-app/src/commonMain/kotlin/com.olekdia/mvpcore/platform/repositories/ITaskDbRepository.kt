package com.olekdia.mvpcore.platform.repositories

import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvpcore.model.entries.TaskEntry

interface ITaskDbRepository : IPlatformComponent {

    fun loadAsync(onComplete: (list: List<TaskEntry>) -> Unit)

    fun load(): ArrayList<TaskEntry>

    fun insert(entry: TaskEntry, pos: Int): TaskEntry

    fun save(entry: TaskEntry)

    fun save(entry: TaskEntry, pos: Int)

    fun delete(entry: TaskEntry)

    fun delete(list: List<TaskEntry>)

    companion object {
        const val COMPONENT_ID: String = "TASK_DB_REP"
    }
}