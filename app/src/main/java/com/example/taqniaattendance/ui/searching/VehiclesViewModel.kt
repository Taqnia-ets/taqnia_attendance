package com.example.taqniaattendance.ui.searching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.example.taqniaattendance.data.model.history.Attendance
import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.history.HistoryResponse
import com.example.taqniaattendance.data.model.history.Punch
import com.example.taqniaattendance.data.model.punch.NewPunch
import com.example.taqniaattendance.data.model.user.User
import com.example.taqniaattendance.data.source.DataSource
import com.example.taqniaattendance.data.source.Repository
import com.example.taqniaattendance.util.Event
import com.example.taqniaattendance.util.LogsUtil
import com.github.mikephil.charting.data.BarEntry
import com.kacst.hsr.data.model.error.AppError
import timber.log.Timber
import java.lang.Exception
import java.text.DateFormat
import java.util.*

class VehiclesViewModel(
    private val repository: Repository
) : ViewModel() {

    val attendanceHistory = MutableLiveData<List<Attendance?>?>()
    val showInfo = MutableLiveData<Boolean>(false)
    val showPunchOptions = MutableLiveData<Boolean>(false)
    val userName = MutableLiveData<String?>()
    val punch = MutableLiveData<String>()
    val snackBarText: MutableLiveData<Event<String?>> = MutableLiveData<Event<String?>>()
    val newPunchAdded = MutableLiveData<Unit>()
    val showLoading = MutableLiveData<Boolean>(false)
    val lastWeekSummaryStatistics = MutableLiveData<List<BarEntry?>?>()
    val weekSummaryFirstDate = MutableLiveData<String?>()
    val weekSummaryLastDate = MutableLiveData<String?>()
    val openHistoryItem = MutableLiveData<Unit>()
    val isEmptyList: LiveData<Boolean> = map(attendanceHistory) {
        it?.isNullOrEmpty()
    }

    init {
//        attendanceHistory.value = getHistory()
        getHistory()
        refreshUser()
//        setPreviousWeekSummary(attendanceHistory.value!!)

    }

    fun getHistory() {
        val historyRequest = HistoryRequest("2020", "8")
        repository.getHistory(historyRequest, object: DataSource.HistoryCallback {
            override fun onGetHistory(historyResponse: HistoryResponse?) {
                val historyAsList = historyResponse?.result?.values?.toList()?.reversed()
                val x =historyAsList?.onEach { if (it.punches.isNullOrEmpty())  it.punches = listOf(Punch())}
                attendanceHistory.value = historyAsList
                historyAsList?.let { setPreviousWeekSummary(it) }
            }

            override fun onFailure(error: AppError) {
                error.message?.let { displaySnackMsg(it) }
            }
        })
    }

    fun buildTestHistory(): List<Attendance> {
        val x0 = Attendance(
            "12/1/2020",
            "12/Feb?2020",
            "8:30 12/1/2020",
            "8:30 12/Feb/2020",
            "Check in",
            listOf(Punch("Mobile", "check in", "9:00 1/Jun/2020", "blaaa"),
                Punch("Device", "check out", "5:00 2/Jun/2020", "blaaa")),
            "12/7/2020",
            "first punch",
            "last punch",
            "8:30 12/1/2020",
            "8:30 12/1/2020",
            "8:30 12/Feb/2020",
            "8:30 12/Feb/2020",
            "5",
            "Five",
            "00:00:00"
        )
        val x1 = Attendance(
            "12/1/2020",
            "12/Feb?2020",
            "8:30 12/1/2020",
            "8:30 12/Feb/2020",
            "Check in",
            listOf(Punch("Mobile", "check in", "9:00 1/Jun/2020", "blaaa"),
                Punch("Device", "check out", "5:00 2/Jun/2020", "blaaa")),
            "13/7/2020",
            "first punch",
            "last punch",
            "8:30 12/1/2020",
            "8:30 12/1/2020",
            "8:30 12/Feb/2020",
            "8:30 12/Feb/2020",
            "5",
            "Five",
            "07:13:00"
        )

        return listOf(x0,x1,x1,x1,x1,x1,x1,x1,x1,x1)


    }

    fun newPunch(lat: Double, long: Double) {
        val newPunch = NewPunch(punchType = punch.value, latitude = lat.toString(), longitude = long.toString())
//        val newPunch = NewPunch(punchType = punch.value, latitude = "24.7192101", longitude = "46.6460797")
        repository.punch(newPunch, object : DataSource.PunchCallback {
            override fun onPunchSuccess() {
                newPunchAdded.value = Unit
                setShowLoading(false)
                LogsUtil.printErrorLog("onPunchSuccess", "good")
            }
            override fun onFailure(error: AppError) {
                error.message?.let { displaySnackMsg(it) }
                LogsUtil.printErrorLog("onFailure", error.message)
            }
        })
    }

    fun showUserInf(){
        showInfo.value = true
    }

    fun togglePunchOptionsView(){
        showPunchOptions.value = showPunchOptions.value?.not()
    }

    fun addNewPunch(punchType: String) {
        punch.value = punchType
    }

    fun displaySnackMsg(msg: String) {
        setShowLoading(false)
        snackBarText.value = Event(msg)
    }

    fun setShowLoading(show: Boolean) {
        showLoading.value = show
    }

    private fun refreshUser() {
        repository.getSavedUser(object : DataSource.UserCallback {
            override fun onGetUser(user: User?) {
                user?.let {
                    userName.value = it.name
                    repository.refreshUserInfo(it)
                }
            }

            override fun onFailure(error: AppError) = Unit
        })
    }

    private fun setPreviousWeekSummary(attendances: List<Attendance?>) {
//        val calender = Calendar.getInstance()
//        calender.firstDayOfWeek = Calendar.MONDAY
//        calender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//        calender.add(Calendar.DATE, -7)
//        print(calender.timeInMillis.toDateFormat())
//        LogsUtil.printErrorLog("first Date", calender.timeInMillis.toDateFormat())

        val calendar = Calendar.getInstance()
        val index = attendances.indexOfFirst {
            calendar.time = it?.getDateAsObject()
            calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
        }
        Timber.e(attendances.get(index).toString())
        val lastWeekAttendances = try {
            attendances.subList(index +1, index + 6).reversed()
        } catch (e : IndexOutOfBoundsException) {
            Timber.e(e)
            emptyList<Attendance>()
        }

        lastWeekAttendances.first()?.getDateAsObject()?.let {
            weekSummaryFirstDate.value = DateFormat.getDateInstance(DateFormat.MEDIUM).format(it)
            val weekLastDate = Calendar.getInstance().apply {
                time = it
                add(Calendar.DAY_OF_MONTH, 6)
            }
            weekSummaryLastDate.value =  DateFormat.getDateInstance(DateFormat.MEDIUM).format(weekLastDate.time)
        }

        lastWeekAttendances.forEach {
            LogsUtil.printDebugLog(it?.date, it?.workingHours)
        }

        lastWeekSummaryStatistics.value = lastWeekAttendances.map {
            BarEntry(index.toFloat(),  it?.workingHours.getDisplayWorkingHours())
        }
    }

    private fun String?.getDisplayWorkingHours() : Float =
        try {
            LogsUtil.printErrorLog("all last week","${this?.substringBefore(":","0")}.${this?.substringAfter(":","0")?.substringBefore(":","0")}f".toFloat().toString())
            "${this?.substringBefore(":","0")?.trim()}.${this?.substringAfter(":","0")?.substringBefore(":","0")?.trim()}f".toFloat()
        } catch (e : Exception) {
            Timber.e(e)
            0f
        }

    fun getItemOrZeroHours(list :List<BarEntry?>? , index : Int) : String {
        lateinit var value : String
        try {
            value = list?.get(index)?.y?.toString() ?: "0.0"
        } catch (e : Exception) {
            value = "0.0"
        }
        return "$value h"
    }


    companion object{
        const val CHECK_IN = "check-in"
        const val CHECK_OUT = "check-out"
    }
//    fun searchForVehicle(
//        value: String,
//        searchType: Int
//    ) = if (searchType == SEARCH_BY_VIN.value) searchVehicleByVin(value)
//    else searchVehicleById(value)
//
//    private fun searchVehicleByVin(vehicleVin: String) {
//        showLoading.postValue(true)
//        repository.searchVehicleByVin(vehicleVin, object : DataSource.SearchVehicleByVinCallback {
//            override fun onGetVehicles(vehiclesList: List<Vehicle>?) {
//                showLoading.postValue(false)
//                vehicles.postValue(vehiclesList)
//                LogsUtil.printErrorLog("vehiclesResponse", vehiclesList.toGson())
//            }
//            override fun onFailure(error: CustomError) {
//                showLoading.postValue(false)
//                errorResponse.postValue(error)
//            }
//        })
//    }
//
//    private fun searchVehicleById(vehicleId: String) {
//        showLoading.postValue(true)
//        repository.searchVehicleById(vehicleId.toInt(), object : DataSource.SearchVehicleByIdCallback {
//            override fun onGetVehicles(vehiclesList: List<Vehicle>?) {
//                showLoading.postValue(false)
//                vehicles.postValue(vehiclesList)
//            }
//            override fun onFailure(error: CustomError) {
//                showLoading.postValue(false)
//                errorResponse.postValue(error)
//            }
//        })
//    }

//    fun onSelectVehicle(vehicle: Vehicle) {
//
//        showLoading.postValue(true)
//        // Vehicle will be saved upon getting its data.
//        repository.getVehicleById(vehicle.id, object : DataSource.GetVehicleByIdCallback {
//            override fun onGetVehicleById(vehicle: Vehicle?) {
//                showLoading.postValue(false)
//                navigation.postValue(
//                    Navigation(HomeActivity::class.java, withClearStack = true)
//                )
//            }
//            override fun onFailure(error: CustomError) {
//                showLoading.postValue(false)
//                errorResponse.postValue(error)
//            }
//        })
//    }
//
//    fun openVehicleDetailsScreen()
//            = navigation.postValue(Navigation(VehicleFormActivity::class.java))
}