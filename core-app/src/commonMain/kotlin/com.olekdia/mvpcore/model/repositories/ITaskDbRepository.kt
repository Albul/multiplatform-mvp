package com.olekdia.mvpcore.model.repositories

import com.olekdia.mvp.platform.IBasePlatformComponent
import com.olekdia.mvpcore.model.entries.TaskEntry

interface ITaskDbRepository : IBasePlatformComponent {

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