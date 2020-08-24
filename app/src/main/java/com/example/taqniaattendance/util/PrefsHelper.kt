package com.example.taqniaattendance.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.taqniaattendance.App
import com.example.taqniaattendance.data.model.LoggedInUser
import com.example.taqniaattendance.data.model.user.User
import com.example.taqniaattendance.util.Constants.PreferenceFile
import com.example.taqniaattendance.util.Constants.Environment
import com.google.gson.Gson


object PrefsHelper {

    private var token: String? = null

    val prefs: (String) -> SharedPreferences = {
        App.getInstance().getSharedPreferences(
            it,
            Context.MODE_PRIVATE
        )
    }

    fun deleteUserData() = with(prefs(PreferenceFile.DEFAULT_PREF).edit()) {
        token = null
        clear()
        apply()
    }

    fun saveUser(user: User?) {
        saveString(
            Constants.User.USER,
            Gson().toJson(user)
        )
    }

    fun getUser() = getJsonObject(
        Constants.User.USER,
        com.example.taqniaattendance.data.model.user.User::class.java
    )

//    fun saveLanguage(language: String) {
//        saveString(
//            LANG.value,
//            language,
//            SETTINGS_PREF.value
//        )
//    }

//    fun getLanguage() = getString(
////        LANG.value,
////        SETTINGS_PREF.value
////    )

    fun saveEnvironment(environment: String) {
        saveString(
            Constants.Environment.KEY_ENVIRONMENT,
            environment,
            PreferenceFile.SETTINGS_PREF
        )
    }

    fun getEnvironment() = getString(
       Environment.KEY_ENVIRONMENT,
        PreferenceFile.SETTINGS_PREF
    )

    fun saveToken(token: String?) {
        saveString(Constants.User.TOKEN, token)
        this.token = token
    }

    fun getToken(): String? {
        LogsUtil.printErrorLog("Auth_Token", ValidationUtil.getString(token))
        return if (token.isNullOrEmpty()) with(prefs(PreferenceFile.DEFAULT_PREF)) {
            getString(Constants.User.TOKEN, "").apply { token = this }
        }
        else token
    }

    fun saveString(
        key: String,
        value: String?,
        prefName: String = PreferenceFile.DEFAULT_PREF
    ) = with(prefs(prefName).edit()) {
        putString(key, value)
        commit()
    }

    fun getString(
        key: String,
        prefName: String = PreferenceFile.DEFAULT_PREF
    ) = prefs(prefName).getString(key, null)


    /**
     * Get json object if exists, else returns null.
     * It is the responsibility of the caller to check nullability of the response.
     */
    fun <T> getJsonObject(
        key: String,
        clazz: Class<T>,
        prefName: String = PreferenceFile.DEFAULT_PREF
    ): T? = with(prefs(prefName)) {
        getString(key, null).run { Gson().fromJson(this, clazz) }
    }

    fun getServerBaseURL() = when (getString(Environment.KEY_ENVIRONMENT, PreferenceFile.SETTINGS_PREF)) {
        Environment.DEVELOPMENT_SERVER_URL -> Environment.DEVELOPMENT_SERVER_URL
        else -> Environment.PRODUCTION_SERVER_URL
    }
}