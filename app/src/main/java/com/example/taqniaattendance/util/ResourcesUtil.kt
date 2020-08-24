package com.example.taqniaattendance.util

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import com.example.taqniaattendance.R
import com.google.android.material.snackbar.Snackbar

// Created by M.Bashaikh on 2019-07-11.

object ResourcesUtil {

    fun isGpsEnabled(context: Context)
            = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)

    fun areNotificationsEnabled(context: Context)
            = NotificationManagerCompat.from(context).areNotificationsEnabled()

    fun getGpsSettingIntent() = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

    fun getNotificationSettingIntent(context: Context) : Intent {
        return Intent().apply {
        action = "android.settings.APP_NOTIFICATION_SETTINGS"
        //for Android 5-7
        putExtra("app_package", context.packageName)
        putExtra("app_uid", context.applicationInfo.uid)
        // for Android 8
        putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
        }
    }

    /**
     * Function to show the "enable GPS" Dialog box
     */
    fun showGPSNotEnabledDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.title_enabel_gps))
            .setMessage(context.getString(R.string.msg_enabel_gps))
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.btn_location_setttings)) { _, _ ->
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

}