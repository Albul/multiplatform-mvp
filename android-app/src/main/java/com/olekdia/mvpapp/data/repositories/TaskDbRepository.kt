package com.olekdia.mvpapp.data.repositories

import com.olekdia.mvp.ISingleComponentFactory
import com.olekdia.mvp.platform.PlatformComponent
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvpapp.common.FutureTask
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpapp.data.entries.extensions.toContentValues
import com.olekdia.mvpapp.data.entries.extensions.toTask
import com.olekdia.mvpapp.data.repositories.db.AppContract
import com.olekdia.mvpapp.data.repositories.db.AppContract.Task
import com.olekdia.mvpapp.data.repositories.db.DbRepository
import com.olekdia.mvpcore.domain.repositories.IDbRepository
import com.olekdia.mvpcore.domain.repositories.ITaskDbRepository
import org.intellij.lang.annotations.Language

class TaskDbRepository : PlatformComponent(),
    ITaskDbRepository {

    override val componentId: String
        get() = ITaskDbRepository.COMPONENT_ID

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
            e.printStackTrace()
        }
        return list
    }

    override fun insert(entry: TaskEntry, pos: Int): TaskEntry =
        dbRep.insert(Task.TABLE, entry.toContentValues(pos))
            .let { taskId ->
                entry.copy(id = taskId)
            }

    override fun save(entry: TaskEntry) {
        dbRep.startUpdate(Task.TABLE, entry.toContentValues(), entry.id)
    }

    override fun save(entry: TaskEntry, pos: Int) {
        dbRep.startUpdate(Task.TABLE, entry.toContentValues(pos), entry.id)
    }

    override fun delete(entry: TaskEntry) {
        dbRep.startDelete(Task.TABLE, entry.id)
    }

    override fun delete(list: List<TaskEntry>) {
        dbRep.startDelete(Task.TABLE, whereIdIn(list))
    }

//--------------------------------------------------------------------------------------------------
//  Details
//--------------------------------------------------------------------------------------------------

    private val dbRep: DbRepository
        get() = platformProvider.get(IDbRepository.COMPONENT_ID)!!


    private fun whereIdIn(list: List<TaskEntry>): String =
        whereColumnInValues(AppContract.BaseColumns._ID, list.map { it.id }, list.size)

    companion object FACTORY : ISingleComponentFactory<IPlatformComponent> {
        override fun invoke(): TaskDbRepository =
            TaskDbRepository()

        @Language("SQLAndroid")
        fun whereColumnInValues(
            columnName: String,
            values: Iterable<Long>,
            size: Int
        ): String = StringBuilder(size * 8).let {   // language=SQLAndroid
            it.append(columnName)
            it.append(" IN (")

            val iterator = values.iterator()
            while (iterator.hasNext()) {
                it.append(iterator.next())
                if (iterator.hasNext()) it.append(',')
            }

            it.append(')')
            it.toString()
        }
    }
}