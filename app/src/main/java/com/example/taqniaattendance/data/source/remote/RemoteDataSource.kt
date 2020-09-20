package com.example.taqniaattendance.data.source.remote

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.taqniaattendance.App
import com.example.taqniaattendance.BuildConfig
import com.example.taqniaattendance.R
import com.example.taqniaattendance.data.model.BaseNetResponse
import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.history.HistoryResponse
import com.example.taqniaattendance.data.model.login.LoginRequest
import com.example.taqniaattendance.data.model.login.LoginResponse
import com.example.taqniaattendance.data.model.notification.Notification
import com.example.taqniaattendance.data.model.punch.NewPunch
import com.example.taqniaattendance.data.model.user.User
import com.example.taqniaattendance.data.source.DataSource
import com.example.taqniaattendance.util.Constants.RemoteMetaData
import com.example.taqniaattendance.util.Constants.ErrorConstants
import com.example.taqniaattendance.util.Constants.GeneralKeys
import com.example.taqniaattendance.util.LogsUtil
import com.example.taqniaattendance.util.NetworkUtil
import com.example.taqniaattendance.util.PrefsHelper
import com.example.taqniaattendance.util.PrefsHelper.getToken
import com.example.taqniaattendance.util.PrefsHelper.saveToken
import com.example.taqniaattendance.data.model.error.AppError
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

object RemoteDataSource : DataSource {

    private const val TIMEOUT_SECONDS: Long = 180
    private lateinit var mEndpoints: ApiEndPoints
    private lateinit var mErrorConverter: Converter<ResponseBody, AppError>

    init {
        initRetrofitClient()
    }

    override fun initRetrofitClient() {
        getRetrofitInstance(PrefsHelper.getServerBaseURL()).apply {
            mEndpoints = create(ApiEndPoints::class.java)
            mErrorConverter = responseBodyConverter(AppError::class.java, arrayOfNulls(0))
        }
    }

    private fun getRetrofitInstance(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClientInstance())
        .build()

    private fun getOkHttpClientInstance(): OkHttpClient {
        OkHttpClient.Builder().apply {
            readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
//            authenticator(TokenAuthenticator())
            addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(RemoteMetaData.CONTENT_TYPE, RemoteMetaData.APPLICATION_JSON)
                    .addHeader(RemoteMetaData.ACCEPT, RemoteMetaData.APPLICATION_JSON)
                    .addHeader(RemoteMetaData.AUTHORIZATION, getToken())
//                    .addHeader(RemoteMetaData.ACCEPT_LANGUAGE, Locale.getDefault().language)
//                    .addHeader(RemoteMetaData.PLATFORM, RemoteMetaData.ANDROID)
//                    .addHeader(RemoteMetaData.APP_SOURCE, RemoteMetaData.GOOGLE_STORE)
//                    .addHeader(RemoteMetaData.APP_VERSION, BuildConfig.VERSION_NAME)
//                    .addHeader(RemoteMetaData.VERSION, Build.VERSION.RELEASE)
//                    .addHeader(RemoteMetaData.MODEL, Build.MODEL)
//                    .addHeader(RemoteMetaData.APP_TYPE, RemoteMetaData.HSR)
                    .build()
                chain.proceed(request)
            }
            // Add interceptor only in Debug mode.
            if (BuildConfig.DEBUG) addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY))
            return build()
        }
    }

    override fun login(loginRequest: LoginRequest, callback: DataSource.LoginCallback) {
        mEndpoints.login(loginRequest).apply {
            enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                    if (response.isSuccessful && response.body()?.isSuccessful() == true) {
                        response.body()?.token?.run {
                            //todo check if there is a need for token to be in "User" object
                            val user = User(id = loginRequest.userID, password = loginRequest.password)
                            PrefsHelper.saveUser(user)
                            saveToken(this)
                        }
                        callback.onLogin(response.body())
                    }
                    else callback.onFailure(getError(response.code(), response.body()?.message))
                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable)
                        = callback.onFailure(getError(-1, t))
            })
        }
    }

    override fun getHistory(historyRequest: HistoryRequest, callback: DataSource.HistoryCallback) {
        mEndpoints.getHistory(historyRequest).apply {
            enqueue(object: Callback<HistoryResponse?> {
                override fun onResponse(
                    call: Call<HistoryResponse?>,
                    response: Response<HistoryResponse?>
                ) {
                    if (response.isSuccessful && response.body()?.isSuccessful() == true) callback.onGetHistory(response.body())
                    else callback.onFailure(getError(response.code(), response.body()?.message))
                }
                override fun onFailure(call: Call<HistoryResponse?>, t: Throwable)
                    = callback.onFailure(getError(-1, t))
            })
        }
    }

    override fun refreshUserInfo(
        user: User,
        callback: DataSource.UserCallback
    ) {
        mEndpoints.getUser().enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful && response.body()?.isSuccessful() == true) {
                    val refreshedUser = response.body()
                    refreshedUser?.apply {
                        password = user.password
                        PrefsHelper.saveUser(this)
                        callback.onGetUser(this)
                    }
                } else callback.onFailure(getError(response.code(), response.body()?.message))
            }
            override fun onFailure(call: Call<User?>, t: Throwable)
            = callback.onFailure(getError(-1, t))
        })
    }

    override fun punch(punch: NewPunch, callback: DataSource.PunchCallback) {
        mEndpoints.punch(punch).enqueue(object : Callback<BaseNetResponse> {
            override fun onResponse(call: Call<BaseNetResponse>, response: Response<BaseNetResponse>) {
                if (response.isSuccessful && response.body()?.isSuccessful() == true) callback.onPunchSuccess()
                else callback.onFailure(getError(response.code(), response.body()?.message))
            }

            override fun onFailure(call: Call<BaseNetResponse>, t: Throwable)
                    = callback.onFailure(getError(-1, t))
        })
    }

    override fun logout(callback: DataSource.LogOutCallback) {

    }

    override fun <T> saveAsJson(key: String, obj: T?) = Unit

    override fun <T> getJsonAsObject(
        key: String,
        clazz: Class<T>,
        callback: DataSource.GetJsonAsObjectCallback
    ) = Unit

    override fun saveEnvironment(environment: String) = Unit



    private fun getError(responseCode: Int, anonymous: Any?): AppError {
        LogsUtil.printErrorLog("Error", responseCode.toString() + "" + anonymous.toString())

        when (responseCode) {
            ErrorConstants.ERROR_CODE_UNAUTHORIZED -> {
                val error = getCustomError(ErrorConstants.ERROR_CODE_UNAUTHORIZED)
                PrefsHelper.deleteUserData()
                sendLocalBroadcast(GeneralKeys.KEY_FORCE_LOG_OUT, error)
                return error
            }
            ErrorConstants.ERROR_CODE_INTERNAL_SERVER_ERROR,
            ErrorConstants.ERROR_CODE_SERVICE_UNAVAILABLE,
            ErrorConstants.ERROR_CODE_RESOURCE_NOT_FOUND ->
                return getCustomError(ErrorConstants.ERROR_CODE_GENERAL)
        }

        when (anonymous) {
            is ResponseBody -> {
                return try {
                    return mErrorConverter.convert(anonymous)
//                    if (error.code == ErrorConstants.ERROR_CODE_FORCE_UPDATE)
//                        sendLocalBroadcast(GeneralKeys.KEY_FORCE_UPDATE, error)
//                 return   AppError(message = error.message)

                } catch (e: IOException) {
                    getCustomError(ErrorConstants.ERROR_CODE_GENERAL)
                }
            }

            is String? -> return AppError(responseCode, anonymous)
            is SocketTimeoutException,
            is UnknownHostException,
            is ConnectException ->
                return getCustomError(ErrorConstants.ERROR_CODE_TIME_OUT)
        }

        return if (!NetworkUtil.isNetworkAvailable()) getCustomError(ErrorConstants.ERROR_CODE_NETWORK)
        else getCustomError(ErrorConstants.ERROR_CODE_GENERAL)
    }

    private fun getCustomError(errorCode: Int) = AppError().apply {
        when (errorCode) {
            ErrorConstants.ERROR_CODE_NETWORK -> {
                code = ErrorConstants.ERROR_CODE_NETWORK
                message = App.getInstance().getString(R.string.error_message_network)
            }
            ErrorConstants.ERROR_CODE_TIME_OUT -> {
                code = ErrorConstants.ERROR_CODE_TIME_OUT
                message = App.getInstance().getString(R.string.error_message_network)
            }
            ErrorConstants.ERROR_CODE_UNAUTHORIZED -> {
                code = ErrorConstants.ERROR_CODE_UNAUTHORIZED
                message = App.getInstance().getString(R.string.error_message_sign_in)
            }
            else -> {
                code = ErrorConstants.ERROR_CODE_GENERAL
                message = App.getInstance().getString(R.string.error_message_general)
            }
        }
    }


    override fun getSavedUser(callback: DataSource.UserCallback)= Unit

    override fun saveUser(user: User?) = Unit

    override fun getUserToken(callback: DataSource.GetUserTokenCallback) = Unit

    override fun saveUserToken(token: String?) = Unit

    override fun deleteUserData() = Unit

    override fun getEnvironment(callback: DataSource.GetEnvironmentCallback) = Unit

    override fun getNotifications(callback: DataSource.NotificationsCallback)  = Unit

    override fun saveNotifications(notification: Notification)  = Unit

    private fun sendLocalBroadcast(
        broadcastKey: String,
        error: AppError?
    ) {
        when (broadcastKey) {
            GeneralKeys.KEY_FORCE_LOG_OUT -> LocalBroadcastManager.getInstance(App.getInstance())
                .sendBroadcast(Intent(GeneralKeys.KEY_FORCE_LOG_OUT))

            GeneralKeys.KEY_FORCE_UPDATE -> {
                val intent = Intent(GeneralKeys.KEY_FORCE_UPDATE).apply {
                    putExtra(GeneralKeys.KEY_BUSINESS_ERROR_MSG, error?.message)
                }
                LocalBroadcastManager.getInstance(App.getInstance())
                    .sendBroadcast(intent)
            }
        }
        LogsUtil.printErrorLog(broadcastKey, error?.message)
    }

//    private class TokenAuthenticator : Authenticator {
//        override fun authenticate(route: Route?, response: okhttp3.Response?): Request? {
//            if (response?.code() != ErrorConstants.ERROR_CODE_UNAUTHORIZED)
//                return null
//            //todo get user name and password from the database
//            val tokenResponse = mEndpoints.login(LoginRequest()).execute()
//            if (tokenResponse.isSuccessful) {
//                saveToken(tokenResponse.body()?.token)
//                val url = response.request().url()
//                    .newBuilder().setQueryParameter(RemoteMetaData.TOKEN, getToken())
//                    .build()
//                return  response.request().newBuilder().url(url).build()
//            }
//            return null
//        }
//    }
}