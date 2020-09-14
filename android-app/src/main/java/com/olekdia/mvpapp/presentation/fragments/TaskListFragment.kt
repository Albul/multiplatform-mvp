@file:Suppress("NOTHING_TO_INLINE")
package com.olekdia.mvpapp.presentation.fragments

import android.os.Bundle
import android.view.*
import android.widget.ListView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBar.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT
import com.google.android.material.button.MaterialButtonToggleGroup
import com.olekdia.common.extensions.ifNotNull
import com.olekdia.mvpapp.presentation.MainActivity
import com.olekdia.mvpapp.R
import com.olekdia.mvpapp.common.extensions.presenterProvider
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpcore.presentation.presenters.ITaskListPresenter
import com.olekdia.mvpcore.presentation.views.ITaskListView
import com.olekdia.mvpapp.presentation.adapters.TaskListAdapter
import com.olekdia.mvpcore.TaskFilter

class TaskListFragment : StatefulFragment(),
    ITaskListView,
    MaterialButtonToggleGroup.OnButtonCheckedListener {

    override val componentId: String
        get() = ITaskListView.COMPONENT_ID

    override val isReady: Boolean
        get() = _isReady

    override var dataProvider: List<TaskEntry>?
        get() = taskListAdapter?.dataProvider
        set(value) {
            taskListAdapter?.dataProvider = value
        }

    override var clearCompletedVisible: Boolean = false
        set(value) {
            field = value
            activity?.invalidateOptionsMenu()
        }

    override fun changeFilter(filter: TaskFilter) {
        taskFilterGroup?.let { group ->
            group.clearOnButtonCheckedListeners()
            group.check(filter.toBtnId())
            group.addOnButtonCheckedListener(this)
        }
    }

    override fun refresh() {
        taskListView?.invalidateViews()
    }

//--------------------------------------------------------------------------------------------------
//  Details
//--------------------------------------------------------------------------------------------------

    private var presenter: ITaskListPresenter? = null
    private var mainActivity: MainActivity? = null

    private var taskFilterParams: ActionBar.LayoutParams? = null
    private var taskFilterGroup: MaterialButtonToggleGroup? = null
    private var taskListView: ListView? = null
    private var taskListAdapter: TaskListAdapter? = null
    private var _isReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = presenterProvider?.getOrCreate(ITaskListPresenter.COMPONENT_ID)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.frag_task_list, container, false)
            .also {
                taskListView = it.findViewById(R.id.task_list)
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter?.onAttach(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity

        taskListAdapter = ifNotNull(
            taskListView, activity, presenter
        ) { listView, activity, presenter ->
            TaskListAdapter(activity, listView, presenter)
        }

        taskFilterGroup = activity
            ?.layoutInflater
            ?.inflate(R.layout.block_task_filter, null, false)
            ?.let { it as? MaterialButtonToggleGroup }
            ?.also { group ->
                group.addOnButtonCheckedListener(this)
            }

        taskFilterParams = ActionBar.LayoutParams(
            WRAP_CONTENT,
            MATCH_PARENT,
            Gravity.CENTER
        )

        _isReady = true
        onForeground()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStart()
    }

    override fun onForeground() {
        super.onForeground()
        setMenuVisibility(true)

        ifNotNull(
            mainActivity, mainActivity?.actionBar
        ) { activity, actionBar ->
            activity.prepareActionbar(componentId)
            activity.setActionBarTitle(
                activity.getString(R.string.tasks)
            )
            actionBar.setCustomView(taskFilterGroup, taskFilterParams)
        }
    }

    override fun onBackground() {
        super.onBackground()
        setMenuVisibility(false)
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

//--------------------------------------------------------------------------------------------------
//  Menu
//--------------------------------------------------------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_frag_task_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.clear_completed_button)
            .setVisible(clearCompletedVisible)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {

            R.id.clear_completed_button -> {
                presenter?.onDeleteCompleted()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(isForeground)
    }

//--------------------------------------------------------------------------------------------------
//  Event handlers
//--------------------------------------------------------------------------------------------------

    override fun onButtonChecked(
        group: MaterialButtonToggleGroup,
        checkedId: Int,
        isChecked: Boolean
    ) {
        presenter?.onChangeFilter(group.getCheckedButtonId().toTaskFilter())
    }

//--------------------------------------------------------------------------------------------------
//  Extensions
//--------------------------------------------------------------------------------------------------

    companion object {

        private inline fun Int?.toTaskFilter(): TaskFilter =
            when (this) {
                R.id.all_filter -> TaskFilter.ALL
                R.id.active_filter -> TaskFilter.ACTIVE
                R.id.completed_filter -> TaskFilter.COMPLETED
                else -> TaskFilter.ALL
            }

        private inline fun TaskFilter.toBtnId(): Int =
            when (this) {
                TaskFilter.ALL -> R.id.all_filter
                TaskFilter.ACTIVE -> R.id.active_filter
                TaskFilter.COMPLETED -> R.id.completed_filter
            }
    }
}