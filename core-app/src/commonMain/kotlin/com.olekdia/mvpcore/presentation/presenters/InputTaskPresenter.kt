package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.common.INVALID_L
import com.olekdia.common.extensions.ifNotNull
import com.olekdia.mvp.presenter.IStatefulViewPresenter
import com.olekdia.mvp.presenter.StatefulViewPresenter
import com.olekdia.mvpcore.TaskPriority
import com.olekdia.mvpcore.domain.IExtModelHolder
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpcore.presentation.IExtPlatformHolder
import com.olekdia.mvpcore.presentation.IExtPresenterHolder
import com.olekdia.mvpcore.presentation.views.IInputTaskView

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

    fun onDiscard()

    fun isStateUnsaved(): Boolean

    fun askDiscardState()

    fun discardState()

    companion object {
        const val COMPONENT_ID: String = "INPUT_TASK_PRES"
    }
}

class InputTaskPresenter : StatefulViewPresenter<IInputTaskView, InputTaskState>(),
    IInputTaskPresenter,
    IExtModelHolder,
    IExtPresenterHolder,
    IExtPlatformHolder {

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
        view?.finish()

        taskListPresenter.onUpdateView()
    }

    override fun onDiscard() {
        discardState()
        view?.finish()
    }

    override fun isStateUnsaved(): Boolean =
        state.initTask != state.currTask

    override fun discardState() {
        state = state.copy(initTask = state.currTask)
    }

    override fun askDiscardState() {
        dialogPresenter.onShowDiscardDlg(IInputTaskView.COMPONENT_ID)
    }

//--------------------------------------------------------------------------------------------------
//  Presenter specific
//--------------------------------------------------------------------------------------------------

    override val componentId: String
        get() = IInputTaskPresenter.COMPONENT_ID

    override var state: InputTaskState =
        InputTaskState() // New creation case by default

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