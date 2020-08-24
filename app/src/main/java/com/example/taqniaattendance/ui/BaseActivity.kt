package com.example.taqniaattendance.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.taqniaattendance.ui.login.LoginActivity
import com.example.taqniaattendance.util.Constants

open class BaseActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBroadcastReceiver()
    }

    private fun registerBroadcastReceiver() {
        val filter = IntentFilter().apply {
            addAction(Constants.GeneralKeys.KEY_FORCE_LOG_OUT)
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mBaseBroadcastReceiver, filter)
    }

    private val mBaseBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            when (intent?.action) {
                Constants.GeneralKeys.KEY_FORCE_LOG_OUT -> {
                    Intent(this@BaseActivity, LoginActivity::class.java).also {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(it)
                        finishAffinity()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(mBaseBroadcastReceiver)
    }

}