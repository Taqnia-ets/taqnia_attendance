package com.example.taqniaattendance


import androidx.room.Room
import com.example.taqniaattendance.data.source.Repository
import com.example.taqniaattendance.util.PrefsHelper
import com.example.taqniaattendance.data.source.local.LocalDataSource
import com.example.taqniaattendance.data.source.remote.RemoteDataSource
import com.example.taqniaattendance.data.source.remote.history.HistoryRemoteDataSource

object ServiceLocator {

    private var repository: Repository? = null
    private var historyDataSource: HistoryRemoteDataSource = HistoryRemoteDataSource

    @JvmStatic
    fun provideRepository(): Repository = synchronized(this) {
        return repository ?: createRepository()
    }


    @JvmStatic
    fun providePreference(): PrefsHelper = synchronized(this) {
        return PrefsHelper
    }

    private fun createRepository(): Repository {
        val newRepo = Repository(
            createLocalDataSource(),
            RemoteDataSource
        )
        repository = newRepo
        return newRepo
    }

    /**
     * You can add constructors to the local data source and return the same instance.
     */
    private fun createLocalDataSource(): LocalDataSource {
        return LocalDataSource(providePreference())
    }

    fun resetRepository() = synchronized(this) {
        repository = null
    }
}
