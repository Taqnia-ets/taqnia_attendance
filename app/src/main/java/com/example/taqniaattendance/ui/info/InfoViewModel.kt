package com.example.taqniaattendance.ui.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taqniaattendance.data.model.user.DepartmentLocation
import com.example.taqniaattendance.data.model.user.Geometry
import com.example.taqniaattendance.data.model.user.User
import com.example.taqniaattendance.data.source.DataSource
import com.example.taqniaattendance.data.source.Repository
import com.kacst.hsr.data.model.error.AppError

class InfoViewModel(
    private val repository: Repository
) : ViewModel() {
    val savedUser = MutableLiveData<User?>()


    init {
        getSavedUser()
    }

    private fun getSavedUser() {
        repository.getSavedUser(object : DataSource.UserCallback {
            override fun onGetUser(user: User?) {
                savedUser.postValue(user)
            }

            override fun onFailure(error: AppError) {
            }
        })
    }



}