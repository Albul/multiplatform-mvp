package com.olekdia.sample.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.olekdia.androidcommon.extensions.ifNotNullAnd
import com.olekdia.sample.R
import com.olekdia.sample.common.AppColors
import com.olekdia.sample.extensions.setTint
import com.olekdia.sample.model.entries.TaskEntry
import com.olekdia.sample.model.entryExtensions.priorityColor
import com.olekdia.sample.presenter.presenters.ITaskListPresenter

class TaskListAdapter(
    private val context: Activity,
    private val listView: ListView,
    private val presenter: ITaskListPresenter
) : BaseAdapter(),
    CompoundButton.OnCheckedChangeListener,
    AdapterView.OnItemClickListener, View.OnClickListener {

    private val inflater: LayoutInflater = context.layoutInflater

    var dataProvider: List<TaskEntry>? = null
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    init {
        listView.onItemClickListener = this
    }

    override fun notifyDataSetChanged() {
        listView.let {
            if (it.adapter == null) {
                it.adapter = this
            } else {
                super.notifyDataSetChanged()
            }
        }
    }

    override fun getCount(): Int {
        return dataProvider?.size ?: 0
    }

    override fun getItem(position: Int): TaskEntry? {
        return dataProvider?.getOrNull(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return bindView(convertView ?: newView(parent), position)
    }

    private fun newView(parent: ViewGroup): View {
        return inflater.inflate(R.layout.item_list_task, parent, false)
            .apply {
                tag = ItemHolder(
                    container = findViewById(R.id.item_container),
                    checkBox = findViewById(R.id.task_checkbox),
                    nameLabel = findViewById(R.id.task_name_label),
                    removeBtn = findViewById<View>(R.id.task_remove_btn)
                        .also {
                            it.setOnClickListener(this@TaskListAdapter)
                        }
                )
            }
    }

    private fun bindView(view: View, position: Int): View {
        (view.tag as? ItemHolder)?.apply {
            val task = getItem(position) ?: return@apply

            checkBox.tag = position
            checkBox.setTint(task.priorityColor)
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = task.isCompleted
            checkBox.jumpDrawablesToCurrentState()
            checkBox.setOnCheckedChangeListener(this@TaskListAdapter)

            nameLabel.text = task.name

            removeBtn.tag = position

            container.setBackgroundColor(
                if (position % 2 == 0) {
                    AppColors.primLight
                } else {
                    AppColors.secLight
                }
            )
        }
        return view
    }

//--------------------------------------------------------------------------------------------------
//  Event handlers
//--------------------------------------------------------------------------------------------------

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        (buttonView?.tag as? Int)?.let { pos ->
            presenter.onToggleCompleted(pos)
        }
    }

    override fun onClick(v: View?) {
        ifNotNullAnd(
            v?.tag as? Int, v?.id == R.id.task_remove_btn
        ) { pos ->
            presenter.onDelete(pos)
        }
    }

    override fun onItemClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        presenter.onEdit(position)
    }

    class ItemHolder(
        val container: View,
        val checkBox: CheckBox,
        val nameLabel: TextView,
        val removeBtn: View
    )
}