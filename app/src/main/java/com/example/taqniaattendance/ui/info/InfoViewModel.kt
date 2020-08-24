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


    private fun getSavedUsTest() {

        savedUser.postValue(User(
            id = "100",
            name = "Mohammed",
            email = "m.bashikh@gmail.com",
            department = "Service Delivery",
            subDepartment = "Development",
            phoneNumber = "055555555",
            departmentLocation = DepartmentLocation(
                type = null,
                geometry = Geometry(listOf(listOf(listOf(
                    46.644956,24.719379),
                listOf(46.645529,24.645529),
                listOf(46.647257,24.718966),
                listOf(46.646752, 24.720051))))
            )
        )
        )

    }

    private fun getSavedUser() {
        repository.getSavedUser(object : DataSource.UserCallback {
            override fun onGetUser(user: User?) {
                savedUser.postValue(user)
            }

            override fun onFailure(error: AppError) {
                TODO("Not yet implemented")
            }
        })
    }



}