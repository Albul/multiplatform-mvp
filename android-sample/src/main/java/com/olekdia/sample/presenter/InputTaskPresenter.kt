package com.olekdia.sample.presenter

import com.olekdia.androidcommon.INVALID_L
import com.olekdia.androidcommon.extensions.ifNotNull
import com.olekdia.mvp.IComponent
import com.olekdia.mvp.presenter.IViewPresenter
import com.olekdia.mvp.presenter.ViewPresenter
import com.olekdia.sample.PresenterId
import com.olekdia.sample.ViewId
import com.olekdia.sample.model.entries.TaskEntry
import com.olekdia.sample.model.models.ITaskModel

data class InputTaskState(
    val initTask: TaskEntry = TaskEntry(),
    val currTask: TaskEntry = initTask.copy()
) {
    fun isNewCreation() =
        currTask.id == INVALID_L
}

interface IInputTaskView : IComponent {

    fun setName(name: String?)

    fun setPriority(priority: Int)

    override val componentId: String
        get() = COMPONENT_ID

    companion object {
        const val COMPONENT_ID: String = ViewId.INPUT_TASK
    }
}

interface IInputTaskPresenter : IViewPresenter<IInputTaskView, InputTaskState> {

    fun onEnterName(name: String)

    fun onPriorityChange(priority: Int)

    fun onApply()

    fun isStateUnsaved(): Boolean

    fun discardState()

    companion object {
        const val COMPONENT_ID: String = PresenterId.INPUT_TASK
    }
}

class InputTaskPresenter : ViewPresenter<IInputTaskView, InputTaskState>(),
    IInputTaskPresenter {

    private var taskModel: ITaskModel? = null

    override fun isStateUnsaved(): Boolean =
        state.initTask != state.currTask

    override fun discardState() {
        state = state.copy(initTask = state.currTask)
    }

    override fun onEnterName(name: String) {
        state = state.copy(currTask = state.currTask.copy(name = name))
        view?.setName(name)
    }

    override fun onPriorityChange(priority: Int) {
        state = state.copy(currTask = state.currTask.copy(priority = priority))
        view?.setPriority(priority)
    }

    override fun onApply() {
        ifNotNull(
            taskModel, state.currTask
        ) { model, task ->
            if (state.isNewCreation()) {
                model.add(task)
            } else {
                model.save(task)
            }
        }
        discardState()

        presenterProvider
            .get<ITaskListPresenter>(ITaskListPresenter.COMPONENT_ID)
            ?.onUpdate()
    }

//--------------------------------------------------------------------------------------------------
//  Presenter lifecycle
//--------------------------------------------------------------------------------------------------

    override val componentId: String
        get() = IInputTaskPresenter.COMPONENT_ID

    override var state: InputTaskState = InputTaskState() // New creation case by default

    override fun onRestoreState(state: InputTaskState) {
        this.state = state
    }

    override fun onCreate() {
        taskModel = modelProvider.get(ITaskModel.COMPONENT_ID)
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