package com.example.taqniaattendance.data.firebase

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taqniaattendance.R
import com.example.taqniaattendance.ServiceLocator
import com.example.taqniaattendance.data.model.notification.Notification
import com.example.taqniaattendance.data.source.DataSource
import com.example.taqniaattendance.ui.login.LoginActivity
import com.example.taqniaattendance.util.Constants
import com.example.taqniaattendance.util.LogsUtil
import com.example.taqniaattendance.util.ValidationUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception
import java.text.DateFormat
import java.util.*
import javax.inject.Inject


class CloudMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        LogsUtil.printErrorLog("CloudMessagingService", p0.toString())
        super.onMessageReceived(p0)
        p0?.let { prepareNotification(it) }
    }

    override fun handleIntent(p0: Intent?) {
        super.handleIntent(p0)

        p0?.extras?.apply {
            val title  = this["title"]?.toString()
            val body =  this["body"]?.toString()
            val date = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH).format(Calendar.getInstance().time) ?: ""
            LogsUtil.printErrorLog("FCM", "title: Value: $title")
            LogsUtil.printErrorLog("FCM", "body: Value: $body")

            if (title == null || body == null)
                return

            try {
                val notification = Notification(title, body, date)
                ServiceLocator.provideRepository().saveNotifications(notification)
                ServiceLocator.provideRepository()
                    .getNotifications(object : DataSource.NotificationsCallback {
                        override fun onGetNotifications(notifications: List<Notification>) {
                            LogsUtil.printErrorLog("saved notifications", notifications.toString())
                        }
                    })
            } catch (e: Exception) {
                LogsUtil.printErrorLog("Crash notifications", e.message)

            }
        }

        }


    private fun prepareNotification(p0: RemoteMessage) {
        val dataMap = p0.getData()
        val title =  if (!TextUtils.isEmpty(dataMap.get("title"))) dataMap.get("title").toString() else p0.notification?.title
        val body =  if (!TextUtils.isEmpty(dataMap.get("body"))) dataMap.get("body").toString() else p0.notification?.body
//        val notificationPayload = NotificationPayload(label = dataMap.get("label"), title = title,
//                body = body, time = DateTimeUtils.convertToEnglishIsoTimeFormat(p0.sentTime))

        displayNotification(title, body)
//        buildLocalOrderBroadcastMessage(notificationPayload)
    }

    private fun displayNotification(title: String?, body: String?) {

        val intent = Intent(this, LoginActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, Constants.GeneralKeys.KEY_GENERAL_NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_taqnia_trans)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }

    }

//    //build Local Broadcast to update the request status
//    internal fun buildLocalOrderBroadcastMessage(notificationPayload: NotificationPayload) {
//        val intent = Intent(Constants.KEY_NOTIFICATION_LOCALE_BROADCAST)
//        intent.putExtra(Constants.KEY_NOTIFICATION_PAYLOAD_OBJECT, notificationPayload)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//    }


}