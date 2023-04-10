package com.heartsun.shivanagarkp.services

import android.content.Context
import androidcommon.extension.logger
import androidx.work.ListenableWorker

import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): ListenableWorker.Result {
        logger( "Performing long running task in scheduled job",TAG)
        // TODO(developer): add long running task here.
        return ListenableWorker.Result.success()
    }

    companion object {
        private const val TAG = "MyWorker"
    }
}