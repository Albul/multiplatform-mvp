package com.olekdia.mvpapp.platform.data.repositories.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import android.os.*
import android.util.Log
import androidx.annotation.IntDef
import com.olekdia.mvp.platform.PlatformComponent
import com.olekdia.mvpcore.platform.data.repositories.IDbRepository
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

    fun startQuery(@Language("SQLAndroid") query: String) {
        val msg = workerThreadHandler.obtainMessage()
        msg.arg1 = EventType.QUERY
        msg.obj = WorkerArgs(
            where = query,
            handler = uiHandler
        )

        workerThreadHandler.sendMessage(msg)
    }

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

    fun startInsert(
        table: String,
        contentValue: ContentValues
    ) {
        startInsert(table, arrayOf(contentValue))
    }

    fun startInsert(
        table: String,
        contentValues: Array<ContentValues>
    ) {
        val msg = workerThreadHandler.obtainMessage()
        msg.arg1 = EventType.INSERT
        msg.obj = WorkerArgs(
            table,
            contentValues = contentValues,
            handler = uiHandler
        )

        workerThreadHandler.sendMessage(msg)
    }

    fun startExecSQL(@Language("SQLAndroid") sql: String) {
        startExecSQL(arrayOf(sql))
    }

    fun startExecSQL(@Language("SQLAndroid") sqls: Array<String>) {
        val msg = workerThreadHandler.obtainMessage()
        msg.arg1 = EventType.EXEC_SQL
        msg.obj = WorkerArgs(sqls = sqls)

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
        id: Int
    ) {
        update(table, contentValue,
            whereId(id.toLong())
        )
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

    fun delete(table: String, id: Int) {
        delete(table,
            whereId(id.toLong())
        )
    }

    fun delete(table: String, id: Long) {
        delete(table, whereId(id))
    }

    fun delete(
        table: String,
        @Language("SQLAndroid") whereClause: String
    ) {
        writableDatabase.delete(table, whereClause, null)
    }

    fun execSQL(@Language("SQLAndroid") sql: String) {
        writableDatabase.execSQL(sql)
    }

    fun execSQL(@Language("SQLAndroid") sqls: Array<String>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (sql in sqls) {
                db.execSQL(sql)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun inTransaction(): TransactionHolder {
        return TransactionHolder(writableDatabase)
    }

//--------------------------------------------------------------------------------------------------
//  Helper classes
//--------------------------------------------------------------------------------------------------

    protected class WorkerArgs(
        val table: String? = null,
        val where: String? = null,
        val contentValues: Array<ContentValues>? = null,
        @Language("SQLAndroid")
        val sqls: Array<String>? = null,
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
                EventType.QUERY -> {
                    args.result = try {
                        val cursor:Cursor? = if (args.where == null)
                            null
                        else
                            rawQuery(args.where)
                        // Calling getCount() causes the cursor window to be filled,
                        // which will make the first access on the main thread a lot faster.
                        cursor?.count
                        cursor
                    } catch (e: Exception) {
                        Log.w(Thread.currentThread().name, e)
                        null
                    }
                }

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

                EventType.EXEC_SQL -> args.sqls?.run {
                    if (this.size == 1) {
                        execSQL(this[0])
                    } else {
                        execSQL(this)
                    }
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
                EventType.QUERY -> { }
                EventType.INSERT -> { }
                EventType.EXEC ->
                    args.futureTask?.postExecute()
            }
        }
    }

    inner class TransactionHolder(private val db: SQLiteDatabase) {

        init {
            db.beginTransaction()
        }

        fun compileStatement(@Language("SQLAndroid") sql: String): SQLiteStatement {
            return db.compileStatement(sql)
        }

        fun insert(table: String, cv: ContentValues): Long {
            return db.insert(table, null, cv)
        }

        fun update(
            table: String,
            cv: ContentValues,
            id: Int
        ): TransactionHolder {
            db.update(table, cv,
                whereId(id.toLong()), null)
            return this
        }

        fun delete(table: String, id: Int): TransactionHolder {
            db.delete(table,
                whereId(id.toLong()), null)
            return this
        }

        fun execSQL(@Language("SQLAndroid") sql: String): TransactionHolder {
            db.execSQL(sql)
            return this
        }

        fun startExecSQL(@Language("SQLAndroid") sql: String): TransactionHolder {
            startExec(object : FutureTask<String?, Any?>(sql) {
                override fun execute(param: String?): Any? {
                    if (param != null) db.execSQL(param)
                    return null
                }
            })
            return this
        }

        fun finish() {
            db.setTransactionSuccessful()
            db.endTransaction()
        }

        fun postFinish() {
            startExec(object : FutureTask<Any?, Any?>() {
                override fun execute(param: Any?): Any? {
                    finish()
                    return null
                }
            })
        }
    }


    @IntDef(
        EventType.QUERY,
        EventType.INSERT,
        EventType.UPDATE,
        EventType.DELETE,
        EventType.EXEC_SQL,
        EventType.EXEC
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class EventType {
        companion object {
            const val QUERY = 1
            const val INSERT = 2
            const val UPDATE = 3
            const val DELETE = 4
            const val EXEC_SQL = 5
            const val EXEC = 6
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