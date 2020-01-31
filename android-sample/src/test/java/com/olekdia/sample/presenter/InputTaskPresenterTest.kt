package com.olekdia.sample.presenter

import com.olekdia.sample.BaseTest
import com.olekdia.sample.TaskPriority
import com.olekdia.sample.model.entries.TaskEntry
import com.olekdia.sample.model.models.ITaskModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InputTaskPresenterTest : BaseTest() {

    val inputPresenter: IInputTaskPresenter = presenterProvider.get(IInputTaskPresenter.COMPONENT_ID)!!
    val taskModel: ITaskModel = modelProvider.get(ITaskModel.COMPONENT_ID)!!

    @Test
    fun presWithoutChange_isStateUnsaved_false() {
        assertFalse(inputPresenter.isStateUnsaved())
    }

    @Test
    fun defultPres_isNewCreation_true() {
        assertNotNull(inputPresenter.state.initTask)
        assertNotNull(inputPresenter.state.currTask)
        assertTrue(inputPresenter.state.isNewCreation())
    }

    @Test
    fun setPresState_isNewCreation_false() {
        inputPresenter.state = InputTaskState(
            TaskEntry(25, 39, "Buy some food", TaskPriority.MEDIUM, false, System.currentTimeMillis())
        )
        assertNotNull(inputPresenter.state.currTask)
        assertFalse(inputPresenter.state.isNewCreation())
    }

    @Test
    fun changeName_isStateUnsaved_true() {
        inputPresenter.onEnterName("Some name")
        assertTrue(inputPresenter.isStateUnsaved())
    }

    @Test
    fun changePriority_isStateUnsaved_true() {
        inputPresenter.onPriorityChange(TaskPriority.HIGH)
        assertTrue(inputPresenter.isStateUnsaved())
    }

    @Test
    fun change_apply_isStateUnsaved_false() {
        inputPresenter.onEnterName("Some task")
        inputPresenter.onPriorityChange(TaskPriority.HIGH)
        assertTrue(inputPresenter.isStateUnsaved())

        inputPresenter.onApply()
        assertFalse(inputPresenter.isStateUnsaved())
    }

    @Test
    fun change_discardState_isStateUnsaved_false() {
        inputPresenter.onEnterName("Some task2")
        inputPresenter.onPriorityChange(TaskPriority.HIGH)
        assertTrue(inputPresenter.isStateUnsaved())

        inputPresenter.discardState()
        assertFalse(inputPresenter.isStateUnsaved())
    }

    @Test
    fun change_stateIsCorrect() {
        val name = "Some task"
        inputPresenter.onEnterName(name)
        assertEquals(inputPresenter.state.currTask.name, name)

        val priority = TaskPriority.MEDIUM
        inputPresenter.onPriorityChange(priority)
        assertEquals(inputPresenter.state.currTask.priority, priority)
    }

    @Test
    fun newTask_apply_newTaskIsCreated() {
        val name = "Buy food"
        inputPresenter.onEnterName(name)
        inputPresenter.onPriorityChange(TaskPriority.HIGH)
        val taskToAdd = inputPresenter.state.currTask
        inputPresenter.onApply()

        assertTrue(
            taskModel.state.taskList!!.contains(taskToAdd)
        )
    }

    @Test
    fun existedTask_apply_taskIsSaved() {
        val existedTask = TaskEntry(
            111, 2, "Buy food", TaskPriority.LOW, false, System.currentTimeMillis()
        )
        taskModel.add(existedTask)

        inputPresenter.state = InputTaskState(existedTask)

        val newName = "Buy lots of food"
        inputPresenter.onEnterName(newName)
        inputPresenter.onPriorityChange(TaskPriority.HIGH)

        inputPresenter.onApply()

        assertNotNull(
            taskModel.state.taskList!!.find { it.id == existedTask.id }
        )

        assertEquals(
            newName,
            taskModel.state.taskList!!.find { it.id == existedTask.id }!!.name
        )
    }
}