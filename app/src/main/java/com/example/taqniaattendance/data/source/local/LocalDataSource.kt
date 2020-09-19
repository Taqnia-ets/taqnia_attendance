package com.example.taqniaattendance.data.source.local

import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.login.LoginRequest
import com.example.taqniaattendance.data.model.notification.Notification
import com.example.taqniaattendance.data.model.punch.NewPunch
import com.example.taqniaattendance.data.model.user.User
import com.example.taqniaattendance.util.PrefsHelper

import com.example.taqniaattendance.data.source.DataSource
import com.google.gson.Gson
import com.example.taqniaattendance.data.model.error.AppError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LocalDataSource(private val prefs: PrefsHelper): DataSource {

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

    override fun saveNotifications(notification: Notification)
            = prefs.saveNotification(notification)

    override fun getNotifications(callback: DataSource.NotificationsCallback)
            = callback.onGetNotifications(prefs.getNotifications())

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

    override fun getHistory(historyRequest: HistoryRequest, callback: DataSource.HistoryCallback) =
        Unit

    override fun refreshUserInfo(
        user: User,
        callback: DataSource.UserCallback
    ) = Unit

    override fun punch(punch: NewPunch, callback: DataSource.PunchCallback)  = Unit

    companion object {
        private var instance: LocalDataSource? = null

        @JvmStatic fun getInstance(prefs: PrefsHelper)
                = instance ?: LocalDataSource(prefs).also{ instance = it }
    }
}