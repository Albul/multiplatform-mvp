package com.olekdia.mvpcore.model.entries

import com.olekdia.mvpcore.TaskPriority

data class TaskEntry(
    var id: Long,
    var pid: Long,
    var name: String,
    var priority: TaskPriority,
    var isCompleted: Boolean,
    var creationLdt: Long
) {
    // New creation constructor
    constructor() : this(
        id = -1L,
        pid = -1L,
        name = "",
        priority = TaskPriority.NONE,
        isCompleted = false,
        creationLdt = -1L // todo
    )
}