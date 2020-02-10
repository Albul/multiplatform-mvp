package com.olekdia.mvpcore.domain.entries

import com.olekdia.common.INVALID_L
import com.olekdia.mvpcore.TaskPriority

data class TaskEntry(
    var id: Long,
    var name: String,
    var priority: TaskPriority,
    var isCompleted: Boolean,
    var creationLdt: Long
) {
    // New creation constructor
    constructor() : this(
        id = INVALID_L,
        name = "",
        priority = TaskPriority.NONE,
        isCompleted = false,
        creationLdt = -1L // todo
    )
}