package com.olekdia.mvpcore.presentation

import com.olekdia.mvpcore.BaseTest
import com.olekdia.mvpcore.TaskPriority
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpcore.domain.models.ITaskModel
import com.olekdia.mvpcore.presentation.presenters.IInputTaskPresenter
import com.olekdia.mvpcore.presentation.presenters.InputTaskState
import kotlin.test.*

class InputTaskPresenterTest : BaseTest() {

    val inputPresenter: IInputTaskPresenter = presenterProvider.get(IInputTaskPresenter.COMPONENT_ID)!!
    val taskModel: ITaskModel = modelProvider.get(
        ITaskModel.COMPONENT_ID)!!

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
    fun onRestorePresState_withExistingTask_isNewCreation_false() {
        inputPresenter.onRestoreState(
            InputTaskState(
                TaskEntry(
                    25,
                    "Buy some food",
                    TaskPriority.MEDIUM,
                    false,
                    1234L
                )
            )
        )
        assertNotNull(inputPresenter.state.currTask)
        assertFalse(inputPresenter.state.isNewCreation())
    }

    @Test
    fun changeName_isStateUnsaved_true() {
        inputPresenter.onNameChange("Some name")
        assertTrue(inputPresenter.isStateUnsaved())
    }

    @Test
    fun changePriority_isStateUnsaved_true() {
        inputPresenter.onPriorityChange(TaskPriority.HIGH)
        assertTrue(inputPresenter.isStateUnsaved())
    }

    @Test
    fun change_apply_isStateUnsaved_false() {
        inputPresenter.onNameChange("Some task")
        inputPresenter.onPriorityChange(TaskPriority.HIGH)
        assertTrue(inputPresenter.isStateUnsaved())

        inputPresenter.onApply()
        assertFalse(inputPresenter.isStateUnsaved())
    }

    @Test
    fun change_discardState_isStateUnsaved_false() {
        inputPresenter.onNameChange("Some task2")
        inputPresenter.onPriorityChange(TaskPriority.HIGH)
        assertTrue(inputPresenter.isStateUnsaved())

        inputPresenter.discardState()
        assertFalse(inputPresenter.isStateUnsaved())
    }

    @Test
    fun change_stateIsCorrect() {
        val name = "Some task"
        inputPresenter.onNameChange(name)
        assertEquals(inputPresenter.state.currTask.name, name)

        val priority = TaskPriority.MEDIUM
        inputPresenter.onPriorityChange(priority)
        assertEquals(inputPresenter.state.currTask.priority, priority)
    }

    @Test
    fun edit_newTask_apply_newTaskIsCreated() {
        val name = "Buy food"
        inputPresenter.onNameChange(name)
        inputPresenter.onPriorityChange(TaskPriority.HIGH)
        val taskToAdd = inputPresenter.state.currTask
        inputPresenter.onApply()

        assertTrue(
            taskModel.state.list!!.any {
                it.name == taskToAdd.name
                        && it.priority == taskToAdd.priority
                        && it.isCompleted == taskToAdd.isCompleted
            }
        )
    }

    @Test
    fun edit_existedTask_apply_taskIsSaved() {
        val existedTask = TaskEntry(
            1111L,
            "Buy food",
            TaskPriority.LOW,
            false,
            1234L
        )
        taskModel.add(existedTask)

        inputPresenter.onRestoreState(
            InputTaskState(existedTask)
        )

        val newName = "Buy lots of food"
        inputPresenter.onNameChange(newName)
        inputPresenter.onPriorityChange(TaskPriority.HIGH)

        inputPresenter.onApply()

        assertNotNull(
            taskModel.state.list!!.find { it.id == existedTask.id }
        )

        assertEquals(
            newName,
            taskModel.state.list!!.find { it.id == existedTask.id }!!.name
        )
    }
}