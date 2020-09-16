package com.example.taqniaattendance.data.source.local

import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.login.LoginRequest
import com.example.taqniaattendance.data.model.punch.NewPunch
import com.example.taqniaattendance.data.model.user.User
import com.example.taqniaattendance.util.PrefsHelper

import com.example.taqniaattendance.data.source.DataSource
import com.google.gson.Gson
import com.kacst.hsr.data.model.error.AppError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LocalDataSource(
    private val prefs: PrefsHelper,
    private val db: WordRoomDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): DataSource {


    override fun getUserToken(callback: DataSource.GetUserTokenCallback) {
        val token = prefs.getToken()
        if (token.isNullOrBlank())
            callback.onFailure(AppError())
        else
            callback.onGetUserToken(token)
    }

    override fun saveUserToken(token: String?) {
        prefs.saveToken(token)
    }

    override fun <T> saveAsJson(key: String, obj: T?) {
        prefs.saveString(key, Gson().toJson(obj))
    }

    override fun initRetrofitClient() = Unit

    override fun <T> getJsonAsObject(
        key: String,
        clazz: Class<T>,
        callback: DataSource.GetJsonAsObjectCallback
    ) {
        callback.onGetJsonAsObject(prefs.getJsonObject(key, clazz))
    }

    override fun getSavedUser(callback: DataSource.UserCallback) {
        callback.onGetUser(prefs.getUser())
    }


    override fun saveUser(user: User?) {
//        launch {
//            val userJson = Gson().toJson(user) ?: ""
//            mUserDao.saveUser(UserEntity(userJson = userJson))
//        }
    }

    override fun getEnvironment(callback: DataSource.GetEnvironmentCallback) =
        callback.onGetEnvironment(prefs.getEnvironment())

    override fun login(
        loginRequest: LoginRequest,
        callback: DataSource.LoginCallback
    ) = Unit

    override fun saveEnvironment(environment: String)
            = prefs.saveEnvironment(environment)

    override fun deleteUserData() {
//        launch {
//            PreferenceHelper.deleteAllData()
//            mUserDao.deleteUser()
//        }
    }

    override fun logout(callback: DataSource.LogOutCallback) = Unit

//    override fun getAllWords(): Result<LiveData<PagedList<Word>>> {
//        val words = db.wordDao.getAllWords().toLiveData(pageSize = 50)
//        return Success(words)
//    }
//
//    /**
//     * This is an example on how to switch main Dispatcher to [ioDispatcher]
//     * using withContext(ioDispatcher).
//     */
//    override suspend fun deleteWords() = withContext(ioDispatcher) {
//        db.wordDao.deleteAll()
//    }
//
//    override suspend fun insertWord(word: Word)
//            = db.wordDao.insertWord(word)
//
//    override suspend fun deleteWord(word: Word)
//            = db.wordDao.deleteWord(word)

    override fun getHistory(historyRequest: HistoryRequest, callback: DataSource.HistoryCallback) =
        Unit

    override fun refreshUserInfo(
        user: User,
        callback: DataSource.UserCallback
    ) = Unit

    override fun punch(punch: NewPunch, callback: DataSource.PunchCallback)  = Unit

    companion object {
        private var instance: LocalDataSource? = null

        @JvmStatic fun getInstance(prefs: PrefsHelper, db: WordRoomDatabase)
                = instance ?: LocalDataSource(prefs, db).also{ instance = it }
    }
}