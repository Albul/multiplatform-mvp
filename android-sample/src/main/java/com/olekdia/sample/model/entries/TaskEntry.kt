package com.olekdia.sample.model.entries

import com.olekdia.androidcommon.INVALID_L
import com.olekdia.sample.TaskPriority

data class TaskEntry(
    var id: Long,
    var pid: Long,
    var name: String,
    @TaskPriority
    var priority: Int,
    var isCompleted: Boolean,
    var creationDt: Long
) {

    // New creation constructor
    constructor() : this(
        id = INVALID_L,
        pid = INVALID_L,
        name = "",
        priority = TaskPriority.ZERO,
        isCompleted = false,
        creationDt = System.currentTimeMillis() // todo
    )
}