package com.olekdia.sample.presenter.presenters

import com.olekdia.androidcommon.extensions.ifNotNullAnd
import com.olekdia.mvp.IComponent
import com.olekdia.mvp.presenter.IViewPresenter
import com.olekdia.mvp.presenter.ViewPresenter
import com.olekdia.sample.Key
import com.olekdia.sample.PresenterId
import com.olekdia.sample.TaskFilter
import com.olekdia.sample.ViewId
import com.olekdia.sample.model.entries.TaskEntry
import com.olekdia.sample.model.models.ITaskModel
import com.olekdia.sample.model.models.TaskListState

interface ITaskListView : IComponent {

    val isReady: Boolean

    var dataProvider: List<TaskEntry>?

    fun refresh()

    override val componentId: String
        get() = COMPONENT_ID

    companion object {
        const val COMPONENT_ID = ViewId.TASK_LIST
    }
}

interface ITaskListPresenter : IViewPresenter<ITaskListView, TaskListState> {
    /**
     *  When task list have been updated from other views
     */
    fun onUpdate()

    fun onToggleCompleted(pos: Int)

    fun onEdit(pos: Int)

    fun onDelete(pos: Int)

    fun onDeleteCompleted()

    fun onChangeFilter(@TaskFilter filter: Int)

    companion object {
        const val COMPONENT_ID: String = PresenterId.TASK_LIST
    }
}

class TaskListPresenter : ViewPresenter<ITaskListView, TaskListState>(),
    ITaskListPresenter {

    var taskModel: ITaskModel? = null

    override fun onToggleCompleted(pos: Int) {
        state.taskList?.getOrNull(pos)?.let { task ->
            taskModel?.complete(task)
            view?.dataProvider = state.taskList
        }
    }

    override fun onUpdate() {
        view?.run {
            ifNotNullAnd(
                state.taskList, isReady
            ) { taskList ->
                dataProvider = taskList
            }
        }
    }

    override fun onEdit(pos: Int) {
        presenterProvider
            .get<IMainViewPresenter>(IMainViewPresenter.COMPONENT_ID)
            ?.showView(
                IInputTaskView.COMPONENT_ID,
                Key.INITIAL_ENTRY to state.taskList?.get(pos)
            )
    }

    override fun onDelete(pos: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeleteCompleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChangeFilter(filter: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//--------------------------------------------------------------------------------------------------
//  Presenter lifecycle
//--------------------------------------------------------------------------------------------------

    override val componentId: String
        get() = ITaskListPresenter.COMPONENT_ID

    override val state: TaskListState
        get() = taskModel!!.state

    override fun onCreate() {
        super.onCreate()
        taskModel = modelProvider.get<ITaskModel>(ITaskModel.COMPONENT_ID)
            ?.also { model ->
                model.loadAsync { onUpdate() }
            }
    }

    override fun onStart() {
        super.onStart()
        onUpdate()
    }
}