package com.example.taqniaattendance.ui.notification

import androidx.lifecycle.*
import com.example.taqniaattendance.data.model.notification.Notification
import com.example.taqniaattendance.data.source.DataSource
import com.example.taqniaattendance.data.source.Repository
import com.example.taqniaattendance.util.LogsUtil

class NotificationsViewModel(
private val repository: Repository
) : ViewModel() {

    val savedNotifications = MutableLiveData<List<Notification>?>()
    val empty: LiveData<Boolean> = Transformations.map(savedNotifications) {
        it.isNullOrEmpty()
    }


    init {
        getNotifications()
    }

    private fun getNotifications() {
        repository.getNotifications(object : DataSource.NotificationsCallback {
            override fun onGetNotifications(notifications: List<Notification>) {
                LogsUtil.printErrorLog("notifications", notifications.toString())
                savedNotifications.value = notifications
            }
        })
    }
}