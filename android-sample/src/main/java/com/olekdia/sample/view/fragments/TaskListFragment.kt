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
import com.olekdia.sample.presenter.presenters.ITaskListPresenter
import com.olekdia.sample.extensions.presenterProvider
import com.olekdia.sample.presenter.presenters.ITaskListView
import com.olekdia.sample.view.adapters.TaskListAdapter

class TaskListFragment : StatefulFragment(),
    ITaskListView {

    private var presenter: ITaskListPresenter? = null

    private var taskListView: ListView? = null
    private var taskListAdapter: TaskListAdapter? = null
    private var _isReady: Boolean = false

    override val isReady: Boolean
        get() = _isReady

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = presenterProvider?.get(ITaskListPresenter.COMPONENT_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.frag_task_list, container, false).also {
            taskListView = it.findViewById(R.id.task_list)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter?.onAttach(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        taskListAdapter = ifNotNull(
            taskListView, activity, presenter
        ) { listView, activity, presenter ->
            TaskListAdapter(activity, listView, presenter)
        }

        _isReady = true
        onForeground()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStart()
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

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _isReady = false
        presenter?.onDetach(this)
    }
}