package com.olekdia.mvpapp.presentation.managers

import android.content.Context
import com.olekdia.mvp.platform.PlatformComponent
import com.olekdia.mvpcore.NumEndingFormat
import com.olekdia.mvpapp.R
import com.olekdia.mvpcore.common.extensions.getNumEndingFormat
import com.olekdia.mvpcore.presentation.managers.ITextManager

class TextManager(private val context: Context) : PlatformComponent(),
    ITextManager {

    override val componentId: String
        get() = ITextManager.COMPONENT_ID

    override val taskCreated: String
        get() = context.getString(R.string.task_created)

    override val taskSaved: String
        get() = context.getString(R.string.task_saved)

    override val undo: String
        get() = context.getString(R.string.undo)

    override val taskDeleted: String
        get() = context.getString(R.string.task_deleted)

    override fun xxTasksDeleted(number: Int): String =
        when (number.getNumEndingFormat()) {
            NumEndingFormat.ONE -> taskDeleted
            NumEndingFormat.X1, NumEndingFormat.X4 ->
                context.getString(R.string.tasks_deleted_4, number)
            NumEndingFormat.X5 ->
                context.getString(R.string.tasks_deleted_5, number)
        }
}