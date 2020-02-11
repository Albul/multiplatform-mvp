package com.olekdia.mvpapp.platform.data.repositories.db

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread

abstract class FutureTask<Param, Result>(
    protected val param: Param? = null
) {
    protected var result: Result? = null
        private set

    @Volatile
    var isCancelled: Boolean = false
        private set

    @UiThread
    open fun preExecute(param: Param?) {
    }

    @WorkerThread
    abstract fun execute(param: Param?): Result?

    @UiThread
    open fun postExecute(result: Result?) {
    }

    fun cancel() {
        isCancelled = true
    }

    @UiThread
    internal fun preExecute() {
        preExecute(param)
    }

    @WorkerThread
    internal fun execute() {
        result = execute(param)
    }

    @UiThread
    internal fun postExecute() {
        postExecute(result)
    }
}