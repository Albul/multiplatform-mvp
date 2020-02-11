package com.olekdia.mvpcore.domain.entries

import com.olekdia.common.INVALID_L
import com.olekdia.mvpcore.TaskPriority

data class TaskEntry(
    var id: Long = INVALID_L,
    var name: String = "",
    var priority: TaskPriority = TaskPriority.NONE,
    var isCompleted: Boolean = false,
    var creationLdt: Long = INVALID_L
)