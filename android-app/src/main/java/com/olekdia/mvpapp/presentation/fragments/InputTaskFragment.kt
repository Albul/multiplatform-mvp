package com.olekdia.mvpapp.presentation.fragments

import android.os.Bundle
import android.view.*
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.button.MaterialButtonToggleGroup
import com.olekdia.androidcommon.NO_RESOURCE
import com.olekdia.androidcommon.extensions.ifNotNull
import com.olekdia.androidcommon.extensions.resetFocus
import com.olekdia.mvpcore.Key
import com.olekdia.mvpapp.presentation.MainActivity
import com.olekdia.mvpapp.R
import com.olekdia.mvpcore.TaskPriority
import com.olekdia.mvpapp.common.extensions.presenterProvider
import com.olekdia.mvpapp.data.entries.parcels.TaskParcel
import com.olekdia.mvpcore.presentation.presenters.IInputTaskPresenter
import com.olekdia.mvpcore.presentation.views.IInputTaskView
import com.olekdia.mvpcore.presentation.presenters.InputTaskState

class InputTaskFragment : StatefulFragment(),
    IInputTaskView,
    TextView.OnEditorActionListener,
    MaterialButtonToggleGroup.OnButtonCheckedListener {

    private var presenter: IInputTaskPresenter? = null

    private var nameEditText: EditText? = null
    private var priorityGroup: MaterialButtonToggleGroup? = null

    override val componentId: String
        get() = IInputTaskView.COMPONENT_ID

    override fun setName(name: String?) {
        nameEditText?.setText(name)
    }

    override fun setPriority(priority: TaskPriority) {
        priorityGroup?.check(priority.toBtnId())
    }

    override fun finish() {
        activity?.onBackPressed()
    }

//--------------------------------------------------------------------------------------------------
//  Fragment lifecycle
//--------------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = presenterProvider?.get(IInputTaskPresenter.COMPONENT_ID)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.frag_input_task, container, false).also {
            nameEditText = it.findViewById<EditText>(R.id.task_name)
                .also { editText ->
                    editText.setOnEditorActionListener(this)
                }
            priorityGroup = it.findViewById<MaterialButtonToggleGroup>(R.id.task_priority_group)
                .also { group ->
                    group.addOnButtonCheckedListener(this)
                }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter?.onAttach(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter?.let { presenter ->
            if (savedInstanceState == null) {
                arguments?.let { args ->
                    args.getParcelable<TaskParcel>(Key.INITIAL_ENTRY)
                        ?.entry
                        ?.let { initEntry ->
                            presenter.onRestoreState(
                                InputTaskState(
                                    initTask = initEntry,
                                    currTask = initEntry.copy()
                                )
                            )
                        }
                }
            } else {
                ifNotNull(
                    savedInstanceState.getParcelable<TaskParcel>(Key.INITIAL_ENTRY)?.entry,
                    savedInstanceState.getParcelable<TaskParcel>(Key.CURRENT_ENTRY)?.entry
                ) { initEntry, currEntry ->
                    presenter.onRestoreState(
                        InputTaskState(
                            initTask = initEntry,
                            currTask = currEntry
                        )
                    )
                }
            }
        }

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
            activity.prepareActionbar(componentId)
            activity.setActionBarTitle(
                activity.getString(
                    if (state.isNewCreation()) R.string.new_task else R.string.edit_task
                )
            )
        }
        nameEditText?.run {
            requestFocus()
        }
    }

    override fun onBackground() {
        super.onBackground()

        setMenuVisibility(isForeground)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter?.state?.putToBundle(outState)
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onDestroyView() {
        presenter?.onDetach(this)
        super.onDestroyView()
    }

    override fun onEternity() {
        super.onEternity()
        resetFocus()

        presenter?.onDestroy()
        presenter = null
    }

    override fun isKeepFragment(): Boolean =
        (presenter?.isStateUnsaved() == true).also { result ->
            if (result) presenter?.askDiscardState()
        }

//--------------------------------------------------------------------------------------------------
//  Menu
//--------------------------------------------------------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_frag_apply, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home,
            R.id.home -> {
                finish()
                true
            }

            R.id.apply_button -> {
                presenter?.onNameChange(nameEditText?.text.toString())
                presenter?.onApply()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

//--------------------------------------------------------------------------------------------------
//  Event handlers
//--------------------------------------------------------------------------------------------------

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE
            || (event != null
                    && event.action == ACTION_UP
                    && event.keyCode == KEYCODE_ENTER)
        ) {
            nameEditText?.text?.toString()?.let {
                presenter?.onNameChange(it)
            }
            resetFocus()
            return true
        }
        return false
    }

    private fun resetFocus() {
        ifNotNull(
            activity, nameEditText, priorityGroup
        ) { activity, editText, group ->
            activity.resetFocus(editText, group)
        }
    }

    override fun onButtonChecked(
        group: MaterialButtonToggleGroup,
        checkedId: Int,
        isChecked: Boolean
    ) {
        presenter?.onPriorityChange(group.checkedButtonId.toTaskPriority())
    }

//--------------------------------------------------------------------------------------------------
//  Extensions
//--------------------------------------------------------------------------------------------------

    companion object {

        private inline fun Int?.toTaskPriority(): TaskPriority =
            when (this) {
                R.id.task_priority_low -> TaskPriority.LOW
                R.id.task_priority_medium -> TaskPriority.MEDIUM
                R.id.task_priority_high -> TaskPriority.HIGH
                else -> TaskPriority.NONE
            }

        private inline fun TaskPriority.toBtnId(): Int =
            when (this) {
                TaskPriority.NONE -> NO_RESOURCE
                TaskPriority.LOW -> R.id.task_priority_low
                TaskPriority.MEDIUM -> R.id.task_priority_medium
                TaskPriority.HIGH -> R.id.task_priority_high
            }

        private inline fun InputTaskState.putToBundle(outState: Bundle) {
            ifNotNull(
                initTask, currTask
            ) { init, curr ->
                outState.putParcelable(
                    Key.INITIAL_ENTRY,
                    TaskParcel(init)
                )
                outState.putParcelable(
                    Key.CURRENT_ENTRY,
                    TaskParcel(curr)
                )
            }
        }
    }
}