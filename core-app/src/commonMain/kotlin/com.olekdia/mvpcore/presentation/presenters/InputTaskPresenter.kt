package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.common.INVALID_L
import com.olekdia.common.extensions.ifNotNull
import com.olekdia.mvp.presenter.IStatefulViewPresenter
import com.olekdia.mvpcore.TaskPriority
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpcore.domain.models.ITaskModel
import com.olekdia.mvpcore.platform.view.views.IInputTaskView
import com.olekdia.mvpcore.presentation.ExtStatefulViewPresenter

data class InputTaskState(
    val initTask: TaskEntry = TaskEntry(),
    val currTask: TaskEntry = initTask.copy()
) {
    fun isNewCreation() =
        currTask.id == INVALID_L
}

interface IInputTaskPresenter : IStatefulViewPresenter<IInputTaskView, InputTaskState> {

    fun onNameChange(name: String)

    fun onPriorityChange(priority: TaskPriority)

    fun onApply()

    fun isStateUnsaved(): Boolean

    fun discardState()

    companion object {
        const val COMPONENT_ID: String = "INPUT_TASK_PRES"
    }
}

class InputTaskPresenter : ExtStatefulViewPresenter<IInputTaskView, InputTaskState>(),
    IInputTaskPresenter {

    override fun isStateUnsaved(): Boolean =
        state.initTask != state.currTask

    override fun discardState() {
        state = state.copy(initTask = state.currTask)
    }

    override fun onNameChange(name: String) {
        state = state.copy(currTask = state.currTask.copy(name = name))
        view?.setName(name)
    }

    override fun onPriorityChange(priority: TaskPriority) {
        state = state.copy(currTask = state.currTask.copy(priority = priority))
        view?.setPriority(priority)
    }

    override fun onApply() {
        ifNotNull(
            taskModel, state.currTask
        ) { model, task ->
            if (state.isNewCreation()) {
                model.insertNew(task)
                toastPresenter.onShow(textManager.taskCreated)
            } else {
                if (model.save(task)) {
                    toastPresenter.onShow(textManager.taskSaved)
                }
                Unit
            }
        }
        discardState()

        taskListPresenter.onUpdateView()
    }

//--------------------------------------------------------------------------------------------------
//  Presenter specific
//--------------------------------------------------------------------------------------------------

    override val componentId: String
        get() = IInputTaskPresenter.COMPONENT_ID

    override var state: InputTaskState =
        InputTaskState() // New creation case by default

    private val taskListPresenter: ITaskListPresenter
        get() = presenterProvider.get(ITaskListPresenter.COMPONENT_ID)!!

    private val taskModel: ITaskModel
        get() = modelProvider.get(ITaskModel.COMPONENT_ID)!!

    override fun onRestoreState(state: InputTaskState) {
        this.state = state
    }

    override fun onStart() {
        ifNotNull(
            view, state.currTask
        ) { v, e ->
            v.setName(e.name)
            v.setPriority(e.priority)
        }
    }
}