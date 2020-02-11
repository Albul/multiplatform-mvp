package com.olekdia.mvpapp.data.repositories.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbOpenHelper(private val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // language=SQLAndroid
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS 'task' (
            _id INTEGER PRIMARY KEY,
            pos INTEGER,
            name TEXT,
            priority INTEGER,
            completed INTEGER,
            creation_date_time INTEGER); 
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private const val LAUNCH_VERSION = 1
        const val DATABASE_VERSION = LAUNCH_VERSION
        const val DB_NAME = "todo_mvp.db"
    }
}