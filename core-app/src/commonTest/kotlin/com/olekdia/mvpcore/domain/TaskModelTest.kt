package com.olekdia.mvpcore.domain

import com.olekdia.mvpcore.BaseTest
import com.olekdia.mvpcore.TaskFilter
import com.olekdia.mvpcore.common.extensions.next
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpcore.domain.repositories.ITaskDbRepository
import com.olekdia.mvpcore.mocks.TaskDbRepositoryMock
import com.olekdia.mvpcore.presentation.singletons.AppPrefs
import io.mockk.spyk
import io.mockk.verify
import kotlin.test.*

class TaskModelTest : BaseTest() {

    val dbRepSpy: ITaskDbRepository = spyk<TaskDbRepositoryMock>().also {
        facade.load(
            platformFactories = arrayOf(ITaskDbRepository.COMPONENT_ID to { it }),
            reloadInstances = true
        )
    }

    @Test
    fun `add(entry) - state is changed, it contains added entry`() {
        val entry = TaskEntry(id = 434)
        val stateBefore = taskModel.state

        taskModel.add(entry)

        assertNotEquals(stateBefore, taskModel.state)
        assertNotNull(taskModel.state.getById(entry.id))
        assertTrue(taskModel.state.list!!.contains(entry))

        verify(exactly = 0) { dbRepSpy.insert(entry, any()) }
    }

    @Test
    fun `add(entry, pos) - state is changed, it contains added entry, pos is correct`() {
        val entry = TaskEntry(id = 434)
        val stateBefore = taskModel.state
        val atPos = stateBefore.list!!.size - 2
        taskModel.add(entry, atPos)

        assertNotEquals(stateBefore, taskModel.state)
        assertNotNull(taskModel.state.getById(entry.id))
        assertTrue(taskModel.state.list!!.contains(entry))
        assertEquals(entry, taskModel.state.list!![atPos])
    }

    @Test
    fun `insertNew() - state is changed, it contains added entry with new id`() {
        val entry = TaskEntry()
        val stateBefore = taskModel.state
        val listSizeBefore = stateBefore.list!!.size
        taskModel.insertNew(entry)

        assertNotEquals(stateBefore, taskModel.state)
        assertNull(taskModel.state.getById(entry.id))
        assertFalse(taskModel.state.list!!.contains(entry))
        assertEquals(listSizeBefore + 1, taskModel.state.list!!.size)

        verify(exactly = 1) { dbRepSpy.insert(entry, any()) }
    }

    @Test
    fun `save() identical entry - entry is not saved, state is not changed`() {
        val stateBefore = taskModel.state

        val entry = stateBefore.list!![1]
        val isSaved = taskModel.save(entry)

        assertFalse(isSaved)
        assertEquals(stateBefore, taskModel.state)

        verify(exactly = 0) { dbRepSpy.save(entry) }
    }

    @Test
    fun `save() modified entry - entry is saved, state is changed, list size is the same`() {
        val stateBefore = taskModel.state

        val entry = stateBefore.list!![1].copy(name = "Bla bla new name")
        val listSizeBefore = stateBefore.list!!.size

        val isSaved = taskModel.save(entry)

        assertTrue(isSaved)
        assertNotEquals(stateBefore, taskModel.state)
        assertEquals(listSizeBefore, taskModel.state.list!!.size)

        verify(exactly = 1) { dbRepSpy.save(entry) }
    }

    @Test
    fun `save() non existing entry - entry is not saved, state is not changed`() {
        val stateBefore = taskModel.state

        val entry = TaskEntry(id = 54354, name = "Very complicated bla bla bla")
        val listSizeBefore = stateBefore.list!!.size

        val isSaved = taskModel.save(entry)

        assertFalse(isSaved)
        assertEquals(stateBefore, taskModel.state)
        assertEquals(listSizeBefore, taskModel.state.list!!.size)

        verify(exactly = 0) { dbRepSpy.save(any()) }
    }

    @Test
    fun `toggleComplete() non existing entry - entry is not saved, state is not changed`() {
        val stateBefore = taskModel.state

        val entry = TaskEntry(id = 54354, name = "Very complicated bla bla bla")
        val listSizeBefore = stateBefore.list!!.size

        taskModel.toggleComplete(entry)

        assertEquals(stateBefore, taskModel.state)
        assertEquals(listSizeBefore, taskModel.state.list!!.size)

        verify(exactly = 0) { dbRepSpy.save(any()) }
    }

    @Test
    fun `toggleComplete() - state is changed, entry's isCompleted is toggled`() {
        val stateBefore = taskModel.state

        val entry = stateBefore.list!![1]
        val listSizeBefore = stateBefore.list!!.size
        val isCompletedBefore = entry.isCompleted

        taskModel.toggleComplete(entry)

        assertNotEquals(stateBefore, taskModel.state)
        assertEquals(listSizeBefore, taskModel.state.list!!.size)
        assertNotEquals(isCompletedBefore, taskModel.state.getById(entry.id)!!.isCompleted)

        verify(exactly = 1) { dbRepSpy.save(any()) }
    }

    @Test
    fun `saveFilter() identical filter - state is not changed`() {
        val stateBefore = taskModel.state
        val filterBefore = stateBefore.filter

        val isSaved = taskModel.saveFilter(filterBefore)

        assertFalse(isSaved)
        assertEquals(stateBefore, taskModel.state)
    }

    @Test
    fun `saveFilter() different filter - state is changed`() {
        val stateBefore = taskModel.state
        val filterBefore = stateBefore.filter

        val filterAfter = filterBefore.next()
        val isSaved = taskModel.saveFilter(filterAfter)

        assertTrue(isSaved)
        assertNotEquals(stateBefore, taskModel.state)
        assertEquals(filterAfter, AppPrefs.taskFilter.getEnumValue())
    }

    @Test
    fun `saveFilter(completed) - filtered list is correct`() {
        taskModel.saveFilter(TaskFilter.COMPLETED)

        assertTrue(taskModel.state.filteredList!!.all { it.isCompleted })
    }

    @Test
    fun `saveFilter(active) - filtered list is correct`() {
        taskModel.saveFilter(TaskFilter.ACTIVE)

        assertTrue(taskModel.state.filteredList!!.all { !it.isCompleted })
    }

    @Test
    fun `saveFilter(all) - filtered list is correct`() {
        taskModel.saveFilter(TaskFilter.ALL)

        assertEquals(taskModel.state.list!!, taskModel.state.filteredList!!)
    }

    @Test
    fun `remove() and delete() non existing entry - state is not changed`() {
        val stateBefore = taskModel.state

        val entry = TaskEntry(id = 54354, name = "Tomato2")

        taskModel.remove(entry)
        assertEquals(stateBefore, taskModel.state)

        taskModel.delete(entry)
        assertEquals(stateBefore, taskModel.state)
    }

    @Test
    fun `remove() - state is changed`() {
        val stateBefore = taskModel.state

        val entry = stateBefore.list!![1]

        taskModel.remove(entry)
        assertNotEquals(stateBefore, taskModel.state)
    }

    @Test
    fun `delete() state is changed`() {
        val stateBefore = taskModel.state

        val entry = stateBefore.list!![1]

        taskModel.delete(entry)
        assertNotEquals(stateBefore, taskModel.state)
        verify(exactly = 1) { dbRepSpy.delete(entry) }
    }

    @Test
    fun `restore(list) - state is changed, list is substituted`() {
        val stateBefore = taskModel.state
        val listBefore = stateBefore.list!!

        taskModel.remove(listBefore[0])
        taskModel.remove(listBefore[1])
        assertNotEquals(stateBefore, taskModel.state)

        taskModel.restore(listBefore)
        assertEquals(stateBefore, taskModel.state)
        assertEquals(listBefore, taskModel.state.list!!)
    }

    @Test
    fun `removeCompleted() - completed entries are gone`() {
        val stateBefore = taskModel.state
        val countCompletedBefore = stateBefore.list!!.count { it.isCompleted }
        if (countCompletedBefore == 0) {
            taskModel.toggleComplete(taskModel.state.list!![0])
        }
        val completedList = taskModel.removeCompleted()
        val countCompletedAfter = taskModel.state.list!!.count { it.isCompleted }

        assertNotNull(completedList)
        assertNotEquals(countCompletedBefore, countCompletedAfter)
        assertEquals(countCompletedBefore, completedList.size)
        assertNotEquals(stateBefore, taskModel.state)
    }

    @Test
    fun `delete(list) - state is changed`() {
        val stateBefore = taskModel.state
        val listSizeBefore = stateBefore.list!!.size
        val toDelete = stateBefore.list!!.take(2)

        taskModel.delete(toDelete)
        assertNotEquals(stateBefore, taskModel.state)
        assertNotEquals(listSizeBefore, taskModel.state.list!!.size)
        assertFalse(taskModel.state.list!!.contains(toDelete[0]))
        assertFalse(taskModel.state.list!!.contains(toDelete[1]))

        verify(exactly = 1) { dbRepSpy.delete(toDelete) }
    }
}