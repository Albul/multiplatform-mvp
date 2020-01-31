package com.olekdia.sample.model.repositories

import android.content.ContentValues
import android.database.Cursor
import com.olekdia.androidcommon.extensions.toBoolean
import com.olekdia.androidcommon.extensions.toInt
import com.olekdia.mvp.model.BaseModel
import com.olekdia.mvp.model.IBaseModel
import com.olekdia.sample.ModelId
import com.olekdia.sample.common.FutureTask
import com.olekdia.sample.model.entries.TaskEntry
import com.olekdia.sample.model.repositories.db.AppContract.Task
import com.olekdia.sample.model.repositories.db.DbRepository
import com.olekdia.sample.model.repositories.db.IDbRepository

interface ITaskDbRepository : IBaseModel {

    fun loadAsync(onComplete: (list: List<TaskEntry>) -> Unit)

    fun load(): ArrayList<TaskEntry>

    fun add(task: TaskEntry, pos: Int)

    fun remove(task: TaskEntry)

    fun save(task: TaskEntry)

    fun save(task: TaskEntry, pos: Int)

    companion object {
        const val COMPONENT_ID: String = ModelId.TASK_DB_REPOSITORY
    }
}

class TaskDbRepository : BaseModel(), ITaskDbRepository {

    override val componentId: String
        get() = ITaskDbRepository.COMPONENT_ID

    private lateinit var dbRep: DbRepository

    override fun onCreate() {
        super.onCreate()
        dbRep = modelProvider.get(IDbRepository.COMPONENT_ID)!!
    }

    override fun loadAsync(onComplete: (list: List<TaskEntry>) -> Unit) {
        dbRep.startExec(
            object : FutureTask<Any, ArrayList<TaskEntry>>() {
                override fun execute(param: Any?): ArrayList<TaskEntry> = load()

                override fun postExecute(result: ArrayList<TaskEntry>?) {
                    onComplete(result!!)
                }
            }
        )
    }

    override fun load(): ArrayList<TaskEntry> {
        val list: ArrayList<TaskEntry> = ArrayList()
        try {
            dbRep.rawQuery( // language=SQLAndroid
                "SELECT ${Task.BASE_PROJECTION} FROM ${Task.TABLE}"
            ).use { cursor ->
                list.ensureCapacity(cursor.count)

                while (cursor.moveToNext()) {
                    list.add(cursor.toTask())
                }
            }
        } catch (e: RuntimeException) {
        }
        return list
    }


    override fun add(task: TaskEntry, pos: Int) {
        dbRep.insert(Task.TABLE, task.toContentValues(pos))
    }

    override fun remove(task: TaskEntry) {
        dbRep.delete(Task.TABLE, task.id)
    }

    override fun save(task: TaskEntry) {
        dbRep.startUpdate(Task.TABLE, task.toContentValues(), task.id)
    }

    override fun save(task: TaskEntry, pos: Int) {
        dbRep.startUpdate(Task.TABLE, task.toContentValues(pos), task.id)
    }
}

fun TaskEntry.toContentValues() =
    ContentValues().also { cv ->
        cv.put(Task._PID, pid)
        cv.put(Task._NAME, name)
        cv.put(Task._PRIORITY, priority)
        cv.put(Task._COMPLETED, isCompleted.toInt())
        cv.put(Task._CREATION_DATE_TIME, creationDt)
    }

fun TaskEntry.toContentValues(pos: Int) =
    toContentValues().also { cv ->
        cv.put(Task._POS, pos)
    }

fun Cursor.toTask(): TaskEntry =
    TaskEntry(
        getLong(Task.ID_),
        getLong(Task.PID_),
        getString(Task.NAME_),
        getInt(Task.PRIORITY_),
        getInt(Task.COMPLETED_).toBoolean(),
        getLong(Task.CREATION_DATE_TIME_)
    )