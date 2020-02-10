package com.olekdia.mvpapp.model.entries.extensions

import android.content.ContentValues
import android.database.Cursor
import com.olekdia.androidcommon.extensions.toBoolean
import com.olekdia.common.extensions.toInt
import com.olekdia.mvpapp.common.AppColors
import com.olekdia.mvpcore.model.entries.TaskEntry
import com.olekdia.mvpapp.model.repositories.db.AppContract
import com.olekdia.mvpapp.platform.extensions.getEnum
import com.olekdia.mvpapp.platform.extensions.put

val TaskEntry.priorityColor: Int
    get() = AppColors.priorityColors[priority.ordinal]

fun TaskEntry.toContentValues() =
    ContentValues().also { cv ->
        cv.put(AppContract.Task._NAME, name)
        cv.put(AppContract.Task._PRIORITY, priority)
        cv.put(AppContract.Task._COMPLETED, isCompleted.toInt())
        cv.put(AppContract.Task._CREATION_DATE_TIME, creationLdt)
    }

fun TaskEntry.toContentValues(pos: Int) =
    toContentValues().also { cv ->
        cv.put(AppContract.Task._POS, pos)
    }

fun Cursor.toTask(): TaskEntry =
    TaskEntry(
        getLong(AppContract.Task.ID_),
        getString(AppContract.Task.NAME_),
        getEnum(AppContract.Task.PRIORITY_),
        getInt(AppContract.Task.COMPLETED_).toBoolean(),
        getLong(AppContract.Task.CREATION_DATE_TIME_)
    )