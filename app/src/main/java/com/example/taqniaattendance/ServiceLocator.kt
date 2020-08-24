package com.example.taqniaattendance


import androidx.room.Room
import com.example.taqniaattendance.data.source.Repository
import com.example.taqniaattendance.util.PrefsHelper
import com.example.taqniaattendance.data.source.local.LocalDataSource
import com.example.taqniaattendance.data.source.local.WordRoomDatabase
import com.example.taqniaattendance.data.source.remote.RemoteDataSource

object ServiceLocator {

    private var database: WordRoomDatabase? = null
    private var repository: Repository? = null

    @JvmStatic
    fun provideRepository(): Repository = synchronized(this) {
        return repository ?: createRepository()
    }

    @JvmStatic
    fun providePreference(): PrefsHelper = synchronized(this) {
        return PrefsHelper
    }

    private fun createDatabase(): WordRoomDatabase {
        val result = Room.databaseBuilder(
            App.getInstance(),
            WordRoomDatabase::class.java,
            "Tasks.db"
        ).build()
        database = result
        return result
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
        val database = database ?: createDatabase()
        return LocalDataSource(providePreference(), database)
    }

    fun resetRepository() = synchronized(this) {
        repository = null
    }
}
