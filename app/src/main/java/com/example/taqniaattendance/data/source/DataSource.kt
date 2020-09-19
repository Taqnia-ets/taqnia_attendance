package com.example.taqniaattendance.data.source

import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.history.HistoryResponse
import com.example.taqniaattendance.data.model.login.LoginRequest
import com.example.taqniaattendance.data.model.login.LoginResponse
import com.example.taqniaattendance.data.model.notification.Notification
import com.example.taqniaattendance.data.model.punch.NewPunch
import com.example.taqniaattendance.data.model.user.User
import com.example.taqniaattendance.data.model.error.AppError


interface DataSource {

    interface BaseCallback {
        fun onFailure(error: AppError)
    }

    interface LoginCallback : BaseCallback {
        fun onLogin(loginResponse: LoginResponse?)
    }
    fun login(loginRequest: LoginRequest, callback: LoginCallback)

    interface HistoryCallback : BaseCallback {
        fun onGetHistory(historyResponse: HistoryResponse?)
    }
    fun getHistory(historyRequest: HistoryRequest, callback: HistoryCallback)

    interface UserCallback : BaseCallback {
        fun onGetUser(user: User?)
    }
    fun refreshUserInfo(
        user: User,
        callback: UserCallback
    )
    fun getSavedUser(callback: UserCallback)

    interface PunchCallback : BaseCallback {
        fun onPunchSuccess()
    }
    fun punch(punch: NewPunch, callback: PunchCallback)

    //--------------------------------------------------------------------
    fun saveUser(user: User?)

    interface GetUserTokenCallback : BaseCallback {
        fun onGetUserToken(token: String?)
    }

    fun getUserToken(callback: GetUserTokenCallback)

    fun saveUserToken(token: String?)

    fun deleteUserData()

    interface NotificationsCallback {
        fun onGetNotifications(notifications : List<Notification>)
    }
    fun getNotifications(callback: NotificationsCallback)
    fun saveNotifications(notification : Notification)

    fun saveEnvironment(environment: String)

    interface GetEnvironmentCallback {
        fun onGetEnvironment(environment: String?)
    }
    fun getEnvironment(callback: GetEnvironmentCallback)

    interface LogOutCallback : BaseCallback {
        fun onLogOut()
    }

    fun logout(callback: LogOutCallback)

    fun <T> saveAsJson(key: String, obj: T?)

    interface GetJsonAsObjectCallback : BaseCallback {
        fun <T> onGetJsonAsObject(obj: T?)
    }

    fun <T> getJsonAsObject(
        key: String,
        clazz: Class<T>,
        callback: GetJsonAsObjectCallback
    )

    fun initRetrofitClient()

//    fun getAllWords(): Result<LiveData<PagedList<Word>>>
//
//    suspend fun deleteWords()
//
//    suspend fun insertWord(word: Word)
//
//    suspend fun deleteWord(word: Word)
}
