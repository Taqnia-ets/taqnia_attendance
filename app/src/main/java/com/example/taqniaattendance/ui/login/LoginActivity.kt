package com.example.taqniaattendance.ui.login

import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.taqniaattendance.R
import com.example.taqniaattendance.data.model.login.LoginRequest
import com.example.taqniaattendance.ui.BaseActivity
import com.example.taqniaattendance.ui.searching.MainActivity
import com.example.taqniaattendance.util.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var notificationToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        refreshNotificationToken()


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        loginViewModel.isLoggedIn.observe(this, Observer {isLoggedIn ->
            animationView.visibility = View.GONE
//            if (isLoggedIn) {
//                showLoginFailed(R.string.invalid_password)
//            }
            if (isLoggedIn) {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finishAffinity()
                }
            }
        })

        loginViewModel.isOTPRequired.observe(this@LoginActivity, Observer {
            animationView.visibility = View.GONE
            llPassword.visibility = View.VISIBLE
        })
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        container.setupSnackbar(this, loginViewModel.snackBarText, Snackbar.LENGTH_LONG)


        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                        username.text.toString(),
                        password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> login(assembleLoginData())
                }
                false
            }

            login.setOnClickListener {
                login(assembleLoginData())
            }
        }
    }

    private fun assembleLoginData(): LoginRequest =
        LoginRequest(
            username.text.toString(),
            password.text.toString(),
            notificationType = notificationToken)

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_LONG).show()
    }

    fun login(loginRequest: LoginRequest) {
        val OTPIsSent = llPassword.isVisible
        if (OTPIsSent && (loginRequest.password.isNullOrBlank() || loginRequest.password.length < 3))
            loginViewModel.snackBarText.postValue(Event(getString(R.string.invalid_otp)))
        else {
            animationView.visibility = View.VISIBLE
            loginViewModel.login(loginRequest)
        }

    }

    private fun refreshNotificationToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    LogsUtil.printErrorLog("NotificationToken", "getInstanceId failed" + task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                token?.let { notificationToken = it }

                // Log and toast
                LogsUtil.printErrorLog("NotificationToken", token)
            })
    }
}



/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}