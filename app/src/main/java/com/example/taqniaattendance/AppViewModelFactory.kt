package com.example.taqniaattendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taqniaattendance.data.source.Repository
import com.example.taqniaattendance.data.source.remote.history.HistoryRemoteDataSource
import com.example.taqniaattendance.ui.info.InfoViewModel
import com.example.taqniaattendance.ui.notification.NotificationsViewModel
import com.example.taqniaattendance.ui.searching.VehiclesViewModel

class AppViewModelFactory(
        private val tasksRepository: Repository // Inject repository in constructor because it may be test double.
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            with (modelClass) {
                when {
                    isAssignableFrom(VehiclesViewModel::class.java) ->
                        VehiclesViewModel(tasksRepository, HistoryRemoteDataSource)

                    isAssignableFrom(InfoViewModel::class.java) ->
                        InfoViewModel(tasksRepository)

                    isAssignableFrom(NotificationsViewModel::class.java) ->
                        NotificationsViewModel(tasksRepository)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }  as T
}