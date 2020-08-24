package com.example.taqniaattendance.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

import com.example.taqniaattendance.R
import com.example.taqniaattendance.data.model.login.LoginRequest
import com.example.taqniaattendance.data.model.login.LoginResponse
import com.example.taqniaattendance.data.source.DataSource
import com.example.taqniaattendance.data.source.Repository
import com.example.taqniaattendance.util.Event
import com.kacst.hsr.data.model.error.AppError

class LoginViewModel(private val repository: Repository) : ViewModel() {


    val isLoggedIn = MutableLiveData<Boolean>()

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    val snackBarText: MutableLiveData<Event<String?>> = MutableLiveData<Event<String?>>()

    init {
        checkLoginState()
    }

    private fun checkLoginState() {
        repository.getUserToken(object : DataSource.GetUserTokenCallback {
            override fun onGetUserToken(token: String?) {
                isLoggedIn.postValue(true)
            }

            override fun onFailure(error: AppError) {
                isLoggedIn.postValue(false)
            }
        })
    }

    fun login(loginRequest: LoginRequest) {
        repository.login(loginRequest, object : DataSource.LoginCallback{
            override fun onLogin(loginResponse: LoginResponse?) {
                isLoggedIn.postValue(true)
            }

            override fun onFailure(error: AppError) {
                isLoggedIn.postValue(false)
                snackBarText.postValue(Event(error.message))
            }
        })
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}