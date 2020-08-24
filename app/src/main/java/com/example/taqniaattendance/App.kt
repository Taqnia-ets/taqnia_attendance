package com.example.taqniaattendance

import android.app.Application
import timber.log.Timber

/**
 * Created by bash on 19/6/20.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    companion object {

        private lateinit var instance: App

        @JvmStatic
        fun getInstance() = instance
    }
}