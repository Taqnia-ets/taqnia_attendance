package com.example.taqniaattendance.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taqniaattendance.data.model.Word
import com.example.taqniaattendance.data.source.local.dao.WordDao



@Database(
    entities = [Word::class],
    version = 1,
    exportSchema = false
)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract val wordDao: WordDao

//    suspend fun deleteWords() = wordDao.deleteAll()
//
//    fun getAllWords(): LiveData<PagedList<Word>>
//            = wordDao.getAllWords().toLiveData(pageSize = 50)
//
//    suspend fun insertWord(word: Word)
//            = wordDao.insertWord(word)
//
//    suspend fun deleteWord(word: Word)
//            = wordDao.deleteWord(word)

    companion object {

        private var instance: WordRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context) = instance
            ?: Room.databaseBuilder(
                context,
                WordRoomDatabase::class.java,
                "word_table")
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
    }
}