package com.example.taqniaattendance.ui.searching

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.taqniaattendance.AppViewModelFactory
import com.example.taqniaattendance.R
import com.example.taqniaattendance.ServiceLocator
import com.example.taqniaattendance.databinding.ActivityHomeBinding
import com.example.taqniaattendance.ui.BaseActivity
import com.example.taqniaattendance.ui.SuccessDialog
import com.example.taqniaattendance.ui.info.InfoFragment
import com.example.taqniaattendance.ui.notification.NotificationsFragment
import com.example.taqniaattendance.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.Executor

class MainActivity : BaseActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val mDialogSuccess by lazy { SuccessDialog(this) }
    private val viewModel by viewModels<MainViewModel> {
        AppViewModelFactory(ServiceLocator.provideRepository())
    }
    private val historyAdapter by lazy { MainAdapter(viewModel) }
    private lateinit var viewDataBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureDataBinding()
        setUpHistoryAdapter()
        subscribeUi()
        setUpBiometric()

    }

    private fun configureDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        with (viewDataBinding) {
            viewmodel = viewModel
            lifecycleOwner = this@MainActivity
        }
    }

    private fun subscribeUi() = with(viewModel) {
        showInfo.observe(this@MainActivity, Observer { showInfo ->
            if (showInfo)
                InfoFragment.newInstance().show(supportFragmentManager, InfoFragment().tag)
        })

        showNotifications.observe(this@MainActivity, Observer { showInfo ->
            if (showInfo)
                NotificationsFragment.newInstance().show(supportFragmentManager, NotificationsFragment().tag)
        })

        showPunchOptions.observe(this@MainActivity, Observer { show ->
            elPunchOptions.toggle()
        })

        punch.observe(this@MainActivity, Observer {
            elPunchOptions.toggle()

            if (isLocationResourcesGranted()) {
                biometricPrompt.authenticate(promptInfo)
            }else {
                requestLocationResources()
            }

        })

        showLoading.observe(this@MainActivity, Observer { showLoading ->
            if (showLoading)
                animationView.show()
            else
                animationView.hide()
        })

        newPunchAdded.observe(this@MainActivity, Observer {
            mDialogSuccess.show()
        })

        viewModel.workingHours.observe(this@MainActivity, Observer {
//            if (viewDataBinding.rcvSummary.adapter != null)
//                return@Observer

            viewDataBinding.rcvSummary.adapter = SummaryAdapter(it?.toDoubleOrNull() ?: 0.0).apply {
                submitList(viewModel.attendanceHistory.value?.take(7)) //don't pass more than 7 items
            }
        })

//        lifecycleScope.launch {
//            viewModel.attendanceHistory.collect {
//                historyAdapter.submitData(it)
//            }
//        }

        expectedLeaveTime.observe(this@MainActivity, Observer {
            if (!it.isNullOrBlank())
                llExpectedLeaveTime.show()
        })

        container.setupSnackbar(this@MainActivity, viewModel.snackBarText, Snackbar.LENGTH_SHORT)

    }

    private fun setUpBiometric(){
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, biometricCallback)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setDeviceCredentialAllowed(true)
            .build()
    }

    private val biometricCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int,
                                           errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            Toast.makeText(applicationContext,
                "Authentication error: $errString", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onAuthenticationSucceeded(
            result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            viewModel.setShowLoading(true)
            LocationUtil.getCurrentLocation(this@MainActivity) {
                LogsUtil.printErrorLog("getCurrentLocation", it.toString())
                it?.run {
                    if (it.isFromMockProvider)
                        viewModel.displaySnackMsg(getString(R.string.fake_location_not_allowed))
                    else
                        viewModel.newPunch(it.latitude, it.longitude)

                    LocationUtil.stopCurrentLocationUpdates(this@MainActivity)
                }
            }
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(applicationContext, "Authentication failed",
                Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun requestLocationResources() {
        if (!ResourcesUtil.isGpsEnabled(this@MainActivity))
            ResourcesUtil.showGPSNotEnabledDialog(this@MainActivity)

        if (!PermissionUtil.isLocationPermissionGranted(this))
            PermissionUtil.requestLocationPermission(this@MainActivity, Constants.UserLocation.REQUEST_CODE_LOCATION)
    }

    private fun isLocationResourcesGranted() : Boolean {
        return ResourcesUtil.isGpsEnabled(this) && PermissionUtil.isLocationPermissionGranted(this)
    }

    private fun setUpHistoryAdapter() = with(viewDataBinding.rcvHistory) {
        adapter = historyAdapter
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.UserLocation.REQUEST_CODE_LOCATION -> {
                if (PermissionUtil.isLocationPermissionGranted(this))
                    biometricPrompt.authenticate(promptInfo)
                else
                    viewModel.displaySnackMsg(getString(R.string.msg_location_permission_required))
            }
        }
    }
}
