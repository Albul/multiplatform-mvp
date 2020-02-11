package com.olekdia.mvpcore.mocks

import com.olekdia.common.INVALID_L
import com.olekdia.mvpcore.TaskPriority
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpcore.domain.repositories.ITaskDbRepository

class TaskDbRepositoryMock : ITaskDbRepository {

    private var nextId: Long = 100

    override fun loadAsync(onComplete: (list: List<TaskEntry>) -> Unit) {
        onComplete(load())
    }

    override fun insert(entry: TaskEntry, pos: Int): TaskEntry =
        entry.copy(id = nextId++)

    override fun load(): ArrayList<TaskEntry> =
        arrayListOf(
            TaskEntry(
                1L,
                "Apples",
                TaskPriority.MEDIUM,
                false,
                INVALID_L
            ),
            TaskEntry(
                2L,
                "Bananas",
                TaskPriority.MEDIUM,
                false,
                INVALID_L
            ),
            TaskEntry(
                3L,
                "Vegetables",
                TaskPriority.MEDIUM,
                false,
                INVALID_L
            ),
            TaskEntry(
                4L,
                "Nuts",
                TaskPriority.MEDIUM,
                false,
                INVALID_L
            )
        )

    override fun save(entry: TaskEntry) {
    }

    override fun save(entry: TaskEntry, pos: Int) {
    }

    override fun delete(entry: TaskEntry) {
    }

    override fun delete(list: List<TaskEntry>) {
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    override val componentId: String
        get() = ITaskDbRepository.COMPONENT_ID
}