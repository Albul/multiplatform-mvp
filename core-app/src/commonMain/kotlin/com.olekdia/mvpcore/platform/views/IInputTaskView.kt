package com.olekdia.mvpcore.platform.views

import com.olekdia.mvp.IComponent
import com.olekdia.mvpcore.TaskPriority

interface IInputTaskView : IComponent {

    fun setName(name: String?)

    fun setPriority(priority: TaskPriority)

    companion object {
        const val COMPONENT_ID: String = "INPUT_TASK_VIEW"
    }
}