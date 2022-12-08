package com.udacity.asteroidradar.worker

import android.content.Context
import android.util.Log
import androidx.work.WorkerParameters
import androidx.work.CoroutineWorker
import androidx.work.Worker
import com.udacity.asteroidradar.database.DatabaseAstroid
import com.udacity.asteroidradar.repository.RepositoryAstroid
import retrofit2.HttpException


class RefreshDataWorker(appContext: Context, params: WorkerParameters):CoroutineWorker(appContext,params) {
    companion object{
        const val WORK_NAME= "get_daily_astroids"
    }

    override suspend fun doWork(): Result {
        val database = DatabaseAstroid.getInstance(applicationContext)
        val repository = RepositoryAstroid(database)

        Log.i("mynetwork", "workmanger started")
        return try {
            repository.refreshAstroids(RepositoryAstroid.AsteroidsFilter.SHOW_ALL)
            Result.success()
        } catch (e: HttpException){
            Result.retry()

        }

    }
}