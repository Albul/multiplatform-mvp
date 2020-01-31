package com.olekdia.sample.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.olekdia.androidcommon.extensions.ifNotNull
import com.olekdia.sample.MainActivity
import com.olekdia.sample.R
import com.olekdia.sample.model.entries.TaskEntry
import com.olekdia.sample.presenter.ITaskListPresenter
import com.olekdia.sample.extensions.presenterProvider
import com.olekdia.sample.presenter.ITaskListView
import com.olekdia.sample.view.adapters.TaskListAdapter

class TaskListFragment : StatefulFragment(),
    ITaskListView {

    private var presenter: ITaskListPresenter? = null

    private var taskListView: ListView? = null
    private var taskListAdapter: TaskListAdapter? = null

    override var dataProvider: List<TaskEntry>?
        get() = taskListAdapter?.dataProvider
        set(value) {
            taskListAdapter?.dataProvider = value
        }

    override fun refresh() {
        taskListView?.invalidateViews()
    }

//--------------------------------------------------------------------------------------------------
//  Fragment lifecycle
//--------------------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.frag_task_list, container, false).also {

            presenter = presenterProvider?.get(ITaskListPresenter.COMPONENT_ID)

            taskListView = it.findViewById(R.id.task_list)

            presenter?.onAttach(this)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        taskListAdapter = ifNotNull(
            taskListView, activity, presenter
        ) { listView, activity, presenter ->
            TaskListAdapter(activity, listView, presenter)
        }

        onForeground()
        //presenter?.platformPresenter?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onForeground() {
        super.onForeground()
        setMenuVisibility(isForeground)
        ifNotNull(
            activity as? MainActivity, presenter?.state
        ) { activity, state ->
            activity.prepareToolbar(componentId)
            activity.setActionBarTitle(
                activity.getString(R.string.tasks)
            )
        }
    }

    override fun onBackground() {
        super.onBackground()
        setMenuVisibility(isForeground)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //presenter?.platformPresenter?.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.onDetach(this)
    }
}