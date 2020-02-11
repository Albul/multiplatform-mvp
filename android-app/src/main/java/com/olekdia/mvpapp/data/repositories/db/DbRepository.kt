package com.olekdia.mvpapp.data.repositories.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.*
import androidx.annotation.IntDef
import com.olekdia.mvp.platform.PlatformComponent
import com.olekdia.mvpapp.common.FutureTask
import com.olekdia.mvpcore.domain.repositories.IDbRepository
import org.intellij.lang.annotations.Language

class DbRepository(val context: Context) : PlatformComponent(),
    IDbRepository {

    val dbOpenHelper: SQLiteOpenHelper = DbOpenHelper(context)

    protected val workerThreadHandler: Handler
    protected val uiHandler: Handler

    val writableDatabase: SQLiteDatabase
        get() = dbOpenHelper.writableDatabase

    val readableDatabase: SQLiteDatabase
        get() = dbOpenHelper.readableDatabase

    init {
        synchronized(DbRepository::class.java) {
            val thread = HandlerThread("DbModelWorker_" + SystemClock.elapsedRealtime())
            thread.start()

            workerThreadHandler = WorkerHandler(thread.looper)
            uiHandler = UiHandler(Looper.getMainLooper())
        }
    }

    override val componentId: String
        get() = IDbRepository.COMPONENT_ID

//--------------------------------------------------------------------------------------------------
//  Async methods
//--------------------------------------------------------------------------------------------------

    fun startUpdate(
        table: String,
        contentValue: ContentValues,
        id: Long
    ) {
        startUpdate(table, contentValue,
            whereId(id)
        )
    }

    fun startUpdate(
        table: String,
        contentValue: ContentValues,
        @Language("SQLAndroid") where: String
    ) {
        startUpdate(table, arrayOf(contentValue), where)
    }

    fun startUpdate(
        table: String,
        contentValues: Array<ContentValues>,
        @Language("SQLAndroid") where: String
    ) {
        val msg = workerThreadHandler.obtainMessage()
        msg.arg1 = EventType.UPDATE
        msg.obj = WorkerArgs(
            table,
            where,
            contentValues
        )

        workerThreadHandler.sendMessage(msg)
    }

    fun startDelete(table: String, id: Long) {
        startDelete(table,
            whereId(id)
        )
    }

    fun startDelete(
        table: String,
        @Language("SQLAndroid") where: String
    ) {
        val msg = workerThreadHandler.obtainMessage()
        msg.arg1 = EventType.DELETE
        msg.obj = WorkerArgs(table, where)

        workerThreadHandler.sendMessage(msg)
    }

    fun startExec(futureTask: FutureTask<out Any?, out Any?>) {
        startExec(futureTask, 0)
    }

    fun startExec(
        futureTask: FutureTask<out Any?, out Any?>,
        delayMillis: Long
    ) {
        val msg = workerThreadHandler.obtainMessage()
        msg.arg1 = EventType.EXEC
        msg.obj = WorkerArgs(
            handler = uiHandler,
            futureTask = futureTask
        )

        futureTask.preExecute()
        if (!futureTask.isCancelled) workerThreadHandler.sendMessageDelayed(msg, delayMillis)
    }

//--------------------------------------------------------------------------------------------------
//  Sync methods
//--------------------------------------------------------------------------------------------------

    fun rawQuery(@Language("SQLAndroid") sql: String): Cursor {
        return readableDatabase.rawQuery(sql, null)
    }

    fun rawQuery(
        @Language("SQLAndroid") sql: String,
        selectionArgs: Array<String>
    ): Cursor {
        return readableDatabase.rawQuery(sql, selectionArgs)
    }

    fun insert(
        table: String,
        contentValue: ContentValues
    ): Long {
        return writableDatabase.insert(table, null, contentValue)
    }

    fun insert(
        table: String,
        contentValues: Array<ContentValues>
    ): Array<Long>? {
        val result:MutableList<Long> = ArrayList(contentValues.size)

        val db:SQLiteDatabase = writableDatabase
        db.beginTransaction()
        try {
            for (cv in contentValues) {
                result.add(db.insert(table, null, cv))
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
        return result.toTypedArray()
    }

    fun update(
        table: String,
        contentValue: ContentValues,
        @Language("SQLAndroid") where: String
    ) {
        writableDatabase.update(table, contentValue, where, null)
    }

    fun update(
        table: String,
        contentValues: Array<ContentValues>,
        @Language("SQLAndroid") where: String
    ) {
        val db:SQLiteDatabase = writableDatabase
        db.beginTransaction()
        try {
            for (cv in contentValues) {
                db.update(table, cv, where, null)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    override fun delete(table: String, id: Int) {
        delete(table,
            whereId(id.toLong())
        )
    }

    override fun delete(table: String, id: Long) {
        delete(table, whereId(id))
    }

    override fun delete(
        table: String,
        @Language("SQLAndroid") whereClause: String
    ) {
        writableDatabase.delete(table, whereClause, null)
    }

//--------------------------------------------------------------------------------------------------
//  Helper classes
//--------------------------------------------------------------------------------------------------

    protected class WorkerArgs(
        val table: String? = null,
        val where: String? = null,
        val contentValues: Array<ContentValues>? = null,
        val futureTask: FutureTask<out Any?, out Any?>? = null,
        val handler: Handler? = null,
        var result: Any? = null
    )

    protected inner class WorkerHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            val args = msg.obj as WorkerArgs

            val token = msg.what
            val event = msg.arg1

            when (event) {
                EventType.INSERT ->
                    if (args.table != null &&
                        args.contentValues != null
                    ) {
                        if (args.contentValues.size == 1) {
                            args.result = insert(args.table, args.contentValues[0])
                        } else {
                            args.result = insert(args.table, args.contentValues)
                        }
                    }

                EventType.UPDATE ->
                    if (args.table != null &&
                        args.where != null &&
                        args.contentValues != null
                    ) {
                        if (args.contentValues.size == 1) {
                            update(args.table, args.contentValues[0], args.where)
                        } else {
                            update(args.table, args.contentValues, args.where)
                        }
                    }

                EventType.DELETE ->
                    if (args.table != null &&
                        args.where != null
                    ) {
                        delete(args.table, args.where)
                    }

                EventType.EXEC -> args.futureTask?.run {
                    if (!isCancelled) {
                        execute()
                        if (isCancelled) return
                    }
                }
            }

            // Passing the original token value back to the caller
            // on top of the event values in arg1
            if (args.handler != null) {
                val reply = args.handler.obtainMessage(token)
                reply.obj = args
                reply.arg1 = msg.arg1

                reply.sendToTarget()
            }
        }
    }

    protected class UiHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            val args = msg.obj as WorkerArgs
            val event = msg.arg1

            when (event) {
                EventType.INSERT -> {
                }
                EventType.EXEC ->
                    args.futureTask?.postExecute()
            }
        }
    }

    @IntDef(
        EventType.INSERT,
        EventType.UPDATE,
        EventType.DELETE,
        EventType.EXEC
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class EventType {
        companion object {
            const val INSERT = 1
            const val UPDATE = 2
            const val DELETE = 3
            const val EXEC = 4
        }
    }

    companion object {
        @JvmStatic
        @Language("SQLAndroid")
        fun whereId(id: Long): String {
            return "_id = $id"
        }
    }
}