package com.olekdia.mvpcore.presentation.presenters

import com.olekdia.common.extensions.ifNotNull
import com.olekdia.common.extensions.ifNotNullAnd
import com.olekdia.mvp.presenter.IStatefulViewPresenter
import com.olekdia.mvp.presenter.StatefulViewPresenter
import com.olekdia.mvpcore.Key
import com.olekdia.mvpcore.TaskFilter
import com.olekdia.mvpcore.ViewType
import com.olekdia.mvpcore.domain.IExtModelHolder
import com.olekdia.mvpcore.domain.models.TaskListState
import com.olekdia.mvpcore.presentation.managers.OnSnackbarStateChangedListener
import com.olekdia.mvpcore.presentation.views.IInputTaskView
import com.olekdia.mvpcore.presentation.views.ITaskListView
import com.olekdia.mvpcore.presentation.IExtPlatformHolder
import com.olekdia.mvpcore.presentation.IExtPresenterHolder

interface ITaskListPresenter : IStatefulViewPresenter<ITaskListView, TaskListState> {

    fun onUpdateView()

    fun onToggleCompleted(pos: Int)

    fun onEdit(pos: Int)

    fun onDelete(pos: Int)

    fun onDeleteCompleted()

    fun onChangeFilter(filter: TaskFilter)

    companion object {
        const val COMPONENT_ID: String = "TASK_LIST_PRES"
    }
}

class TaskListPresenter : StatefulViewPresenter<ITaskListView, TaskListState>(),
    ITaskListPresenter,
    IExtModelHolder,
    IExtPresenterHolder,
    IExtPlatformHolder {

    override val componentId: String
        get() = ITaskListPresenter.COMPONENT_ID

    override fun onUpdateView() {
        view?.run {
            ifNotNullAnd(
                state.filteredList, isReady
            ) { entryList ->
                if (dataProvider !== entryList) {
                    dataProvider = entryList
                }
            }
            changeFilter(state.filter)
            state.hasCompleted().let { completedVisible ->
                if (clearCompletedVisible != completedVisible) {
                    clearCompletedVisible = completedVisible
                }
            }
        }
    }

    override fun onToggleCompleted(pos: Int) {
        state.filteredList
            ?.getOrNull(pos)
            ?.let { targetEntry ->
                taskModel.toggleComplete(targetEntry)
                onUpdateView()
            }
    }

    override fun onEdit(pos: Int) {
        state.filteredList
            ?.getOrNull(pos)
            ?.let { targetEntry ->
                mainViewPresenter.showView(
                    IInputTaskView.COMPONENT_ID,
                    ViewType.FORM,
                    arrayOf(Key.INITIAL_ENTRY to targetEntry)
                )
            }
    }

    override fun onDelete(pos: Int) {
        state.filteredList
            ?.getOrNull(pos)
            ?.let { affectedEntry ->
                taskModel.remove(affectedEntry)
                onUpdateView()

                snackPresenter.onShow(
                    textManager.taskDeleted,
                    textManager.undo,
                    object :
                        OnSnackbarStateChangedListener {
                        override fun onUndo() {
                            taskModel.add(affectedEntry, pos)
                            onUpdateView()
                        }

                        override fun onApply() {
                            taskModel.delete(affectedEntry)
                        }
                    },
                    inFormView = false
                )
            }
    }

    override fun onDeleteCompleted() {
        ifNotNull(
            state.list, taskModel.removeCompleted()
        ) { sourceList, removedList ->
            onUpdateView()

            snackPresenter.onShow(
                textManager.xxTasksDeleted(removedList.size),
                textManager.undo,
                object :
                    OnSnackbarStateChangedListener {
                    override fun onUndo() {
                        taskModel.restore(sourceList)
                        onUpdateView()
                    }

                    override fun onApply() {
                        taskModel.delete(removedList)
                    }
                },
                inFormView = false
            )
        }
    }

    override fun onChangeFilter(filter: TaskFilter) {
        if (taskModel.saveFilter(filter)) {
            onUpdateView()
        }
    }

//--------------------------------------------------------------------------------------------------
//  Details
//--------------------------------------------------------------------------------------------------

    override val state: TaskListState
        get() = taskModel.state

    override fun onStart() {
        super.onStart()
        onUpdateView()
    }
}