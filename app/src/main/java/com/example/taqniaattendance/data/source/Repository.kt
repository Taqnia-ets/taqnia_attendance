package com.example.taqniaattendance.data.source

import com.example.taqniaattendance.data.Result
import com.example.taqniaattendance.data.model.LoggedInUser
import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.login.LoginRequest
import com.example.taqniaattendance.data.model.punch.NewPunch
import com.example.taqniaattendance.data.model.user.User
import com.kacst.hsr.data.model.error.AppError

class Repository(
    private val localDataSource: DataSource,
    private val remoteDataSource: DataSource
) : DataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = Result.Success<LoggedInUser>(LoggedInUser("Fgsdf","fedsg"))

        return result
    }

    override fun getHistory(historyRequest: HistoryRequest, callback: DataSource.HistoryCallback) {
        remoteDataSource.getHistory(historyRequest, callback)
    }

    override fun refreshUserInfo(
        user: User,
        callback: DataSource.UserCallback
    ) {
        remoteDataSource.refreshUserInfo(user, callback)
    }

    override fun punch(punch: NewPunch, callback: DataSource.PunchCallback) {
        remoteDataSource.punch(punch, callback)
    }

    //-----------------------------------------------------------------------------------------
    override fun getUserToken(callback: DataSource.GetUserTokenCallback)
            = localDataSource.getUserToken(callback)

    override fun saveUserToken(token: String?)
            = localDataSource.saveUserToken(token)

    override fun getEnvironment(callback: DataSource.GetEnvironmentCallback)
            = localDataSource.getEnvironment(callback)

    override fun deleteUserData() = localDataSource.deleteUserData()

    override fun login(
        loginRequest: LoginRequest,
        callback: DataSource.LoginCallback
    ) = remoteDataSource.login(loginRequest, callback)

    override fun getSavedUser(callback: DataSource.UserCallback)
            = localDataSource.getSavedUser(callback)

    override fun saveUser(user: User?) = localDataSource.saveUser(user)

    override fun logout(callback: DataSource.LogOutCallback) {
        remoteDataSource.logout(object: DataSource.LogOutCallback {
            override fun onFailure(error: AppError) {
                localDataSource.deleteUserData()
                callback.onFailure(error)
            }
            override fun onLogOut() {
                localDataSource.deleteUserData()
                callback.onLogOut()
            }
        })
    }

    override fun <T> saveAsJson(key: String, obj: T?)
            = localDataSource.saveAsJson(key, obj)

    override fun <T> getJsonAsObject(
        key: String,
        clazz: Class<T>,
        callback: DataSource.GetJsonAsObjectCallback
    ) = localDataSource.getJsonAsObject(key, clazz, callback)

    override fun saveEnvironment(environment: String)
            = localDataSource.saveEnvironment(environment)

    override fun initRetrofitClient()
            = remoteDataSource.initRetrofitClient()

//    suspend fun deleteWords()
//            = localDataSource.deleteWords()
//
//    /**
//     * Check how you can use error object in this situation..!!!
//     */
//    fun getAllWords(): LiveData<PagedList<Word>> {
//        val words = localDataSource.getAllWords() as Success
//        return words.data
//    }
//
//    suspend fun insertWord(word: Word)
//            = localDataSource.insertWord(word)
//
//    suspend fun deleteWord(word: Word)
//            = localDataSource.deleteWord(word)

    companion object {

    }
}