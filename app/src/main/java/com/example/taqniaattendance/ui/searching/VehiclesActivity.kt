package com.example.taqniaattendance.ui.searching

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.example.taqniaattendance.util.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.Executor

class VehiclesActivity : BaseActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val DAYS =
        arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    private val mDialogSuccess by lazy { SuccessDialog(this) }
    private val viewModel by viewModels<VehiclesViewModel> {
        AppViewModelFactory(ServiceLocator.provideRepository())
    }


    private lateinit var viewDataBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureDataBinding()
        setUpVehiclesAdapter()
        subscribeUi()
        setUpBiometric()

    }

    private fun configureDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        with (viewDataBinding) {
            viewmodel = viewModel
            lifecycleOwner = this@VehiclesActivity
        }
    }

    private fun subscribeUi() = with(viewModel) {
        showInfo.observe(this@VehiclesActivity, Observer { showInfo ->
            if (showInfo)
                InfoFragment.newInstance().show(supportFragmentManager, InfoFragment().tag)
        })

        showPunchOptions.observe(this@VehiclesActivity, Observer { show ->
            elPunchOptions.toggle()
        })

        punch.observe(this@VehiclesActivity, Observer {
            elPunchOptions.toggle()

            if (isLocationResourcesGranted()) {
                biometricPrompt.authenticate(promptInfo)
            }else {
                requestLocationResources()
            }

        })

        showLoading.observe(this@VehiclesActivity, Observer {showLoading ->
            if (showLoading)
                animationView.show()
            else
                animationView.hide()
        })

        newPunchAdded.observe(this@VehiclesActivity, Observer {
            mDialogSuccess.show()
        })

        lastWeekSummaryStatistics.observe(this@VehiclesActivity, Observer {
            LogsUtil.printDebugLog("lastWeekSummaryStatistics", it.toString())
            it?.let { it1 -> setupSummaryChart(it1) }
        })

        container.setupSnackbar(this@VehiclesActivity, viewModel.snackBarText, Snackbar.LENGTH_SHORT)


//        searchState.observe(this@VehiclesActivity, Observer {
//            showSearchState(it)
//        })
//        errorResponse.observe(this@VehiclesActivity, Observer {
//            showMessage(btnSearch, it, ViewType.SNACKBAR.value)
//        })
//        navigation.observe(this@VehiclesActivity, Observer {
//            if (it.withClearStack) goWithFinishAffinity(this@VehiclesActivity, it.destination, null)
//            else  goToActivity(this@VehiclesActivity, it.destination, null)
//        })
//        showLoading.observe(this@VehiclesActivity, Observer { needLoading ->
//            if (needLoading) showLoading() else hideLoading()
//        })
//        tbSearchType.onTabSelected {
//            tvVinOrIdErr.remove()
//            edtVinOrId.hint = when(it?.position) {
//                SEARCH_BY_VIN.value -> getString(R.string.txt_vin_last_6_numbers_hint)
//                SEARCH_BY_ID.value ->  getString(R.string.txt_vehicle_id_hint)
//                else -> ""
//            }
//        }
//        edtVinOrId.apply {
//            onTextChanged { tvVinOrIdErr.remove() }
//            onActionDoneClick { validateInputs() }
//        }
//        btnSearch.onClick { validateInputs() }
//        btnClear.onClick { edtVinOrId.clear() }
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
            LocationUtil.getCurrentLocation(this@VehiclesActivity) {
                LogsUtil.printErrorLog("getCurrentLocation", it.toString())
                it?.run {
                    if (it.isFromMockProvider)
                        viewModel.displaySnackMsg(getString(R.string.fake_location_not_allowed))
                    else
                        viewModel.newPunch(it.latitude, it.longitude)

                    LocationUtil.stopCurrentLocationUpdates(this@VehiclesActivity)
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

    private fun setupSummaryChart(bars : List<BarEntry?>) {

        bars.forEachIndexed { index, barEntry ->  barEntry?.x = index.toFloat()}


        val barDataSet = BarDataSet(bars, "days")
        barDataSet.setDrawValues(false)
        barDataSet.color = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        val barData = BarData( barDataSet)
        barData.barWidth = 0.22f

        val xaixFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toString()}h"
            }
        }
        bcSummary.xAxis.valueFormatter = xaixFormatter
        bcSummary.axisLeft.apply {
            disableGridDashedLine()
            setDrawGridLines(false)
            isEnabled = false
            axisMinimum = 1f
            axisMaximum = 7f
            setDrawZeroLine(true)
        }

        bcSummary.axisRight.apply {
            disableGridDashedLine()
            setDrawGridLines(false)
            isEnabled = false
            axisMinimum = 1f
            axisMaximum = 7f
            setDrawZeroLine(true)
        }

        bcSummary.xAxis.apply {
            disableGridDashedLine()
            setDrawGridLines(false)
            setDrawLabels(false)
            isEnabled = false
        }

        bcSummary.setDrawGridBackground(false)
        bcSummary.setGridBackgroundColor(Color.TRANSPARENT)
        bcSummary.setScaleEnabled(false)
        bcSummary.description.isEnabled = false
        bcSummary.data = barData
        bcSummary.legend.isEnabled = false
        bcSummary.invalidate()
    }

    private fun requestLocationResources() {
        if (!ResourcesUtil.isGpsEnabled(this@VehiclesActivity))
            ResourcesUtil.showGPSNotEnabledDialog(this@VehiclesActivity)

        if (!PermissionUtil.isLocationPermissionGranted(this))
            PermissionUtil.requestLocationPermission(this@VehiclesActivity, Constants.UserLocation.REQUEST_CODE_LOCATION)
    }

    private fun isLocationResourcesGranted() : Boolean {
        return ResourcesUtil.isGpsEnabled(this) && PermissionUtil.isLocationPermissionGranted(this)
    }


    private fun setUpVehiclesAdapter() = with(viewDataBinding.rcvVehicles) {
        adapter = VehiclesAdapter(viewModel)
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
