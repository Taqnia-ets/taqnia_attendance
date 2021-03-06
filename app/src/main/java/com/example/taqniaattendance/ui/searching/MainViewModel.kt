package com.example.taqniaattendance.ui.searching

import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taqniaattendance.data.model.history.Attendance
import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.history.HistoryResponse
import com.example.taqniaattendance.data.model.history.Punch
import com.example.taqniaattendance.data.model.punch.NewPunch
import com.example.taqniaattendance.data.model.user.User
import com.example.taqniaattendance.data.source.DataSource
import com.example.taqniaattendance.data.source.Repository
import com.example.taqniaattendance.data.source.remote.history.HistoryRemoteDataSource
import com.example.taqniaattendance.util.*
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.example.taqniaattendance.data.model.error.AppError
import timber.log.Timber
import java.lang.Exception
import java.text.DateFormat
import java.util.*

class MainViewModel(
    private val repository: Repository,
    private val historyDataSource : HistoryRemoteDataSource
) : ViewModel() {

    val attendanceHistory = MutableLiveData<List<Attendance?>?>()
    val showInfo = MutableLiveData<Boolean>(false)
    val showNotifications = MutableLiveData<Boolean>(false)
    val showPunchOptions = MutableLiveData<Boolean>(false)
    val userName = MutableLiveData<String?>()
    val workingHours = MutableLiveData<Double?>()
    val expectedLeaveTime = MutableLiveData<String?>()
    val totalAbsentDays = MutableLiveData<String?>()
    val totalLateTimes = MutableLiveData<String?>()
    val punch = MutableLiveData<String>()
    val snackBarText: MutableLiveData<Event<String?>> = MutableLiveData<Event<String?>>()
    val newPunchAdded = MutableLiveData<Unit>()
    val showLoading = MutableLiveData<Boolean>(false)
    val weekSummaryFirstDate = MutableLiveData<String?>()
    val weekSummaryLastDate = MutableLiveData<String?>()
    val openHistoryItem = MutableLiveData<Unit>()

    private fun refreshNotificationToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    LogsUtil.printErrorLog("NotificationToken", "getInstanceId failed" + task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                LogsUtil.printErrorLog("NotificationToken", token)
            })
    }

//    val listData = Pager(PagingConfig(pageSize = 31)) {
//        historyDataSource
//    }.flow.cachedIn(viewModelScope)

    init {
//        attendanceHistory.value = buildTestHistory()
        refreshUser()
        getHistory()
//        setPreviousWeekSummary(attendanceHistory.value!!)

    }

    fun getHistory() {
        val historyRequest = getInitHistoryRequest()
        repository.getHistory(historyRequest, object: DataSource.HistoryCallback {
            override fun onGetHistory(historyResponse: HistoryResponse?) {
                totalAbsentDays.value = historyResponse?.result?.absentDays
                totalLateTimes.value = historyResponse?.result?.totalAttendLateTimeStr
                val historyAsList = historyResponse?.result?.attendances?.values?.toList()?.reversed()
//                val x =historyAsList?.onEach { if (it.punches.isNullOrEmpty())  it.punches = listOf(Punch())}
                historyAsList?.let {
                    attendanceHistory.value = it
                    setPreviousWeekSummary(it.take(7))
                    expectedLeaveTime.value = getExpectedLeaveTime(it.first(), workingHours.value)
                }
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
            "07:15:00"
        )

        val history = listOf(x0,x1,x1,x1,x1,x1,x1,x1,x1)
        setPreviousWeekSummary(history.take(7))
        return history


    }

    fun newPunch(lat: Double, long: Double) {
        val newPunch = NewPunch(punchType = punch.value, latitude = lat.toString(), longitude = long.toString())
//        val newPunch = NewPunch(punchType = punch.value, latitude = "24.7192101", longitude = "46.6460797")
        repository.punch(newPunch, object : DataSource.PunchCallback {
            override fun onPunchSuccess() {
                getHistory()
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

    fun showNotifications() {
        showNotifications.value = true
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
                workingHours.value = user?.workingHours.getDisplayWorkingHours() ?: DEFAULT_WORKING_HOURS
                user?.let {
                    userName.value = it.name
                    repository.refreshUserInfo(it, getUserCallback())
                }
            }
            override fun onFailure(error: AppError) {
                workingHours.value = DEFAULT_WORKING_HOURS
            }
        })
    }

    private fun getUserCallback() = object : DataSource.UserCallback {
        override fun onGetUser(user: User?) {
            user?.let {
                //if there is no data then it sets in getSavedUser, this will helps in first time user opens the app
                userName.value = it.name
                workingHours.value = it.workingHours.getDisplayWorkingHours() ?: DEFAULT_WORKING_HOURS
            }
        }
        override fun onFailure(error: AppError) {

        }
    }
    private fun getInitHistoryRequest() : HistoryRequest {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1 //we add 1, according that Calendar.MONTH start from 0
        return HistoryRequest(calendar.get(Calendar.YEAR).toString(), month.toString())
    }

    private fun setPreviousWeekSummary(attendances: List<Attendance?>) {

        attendances.first()?.getDateAsObject()?.let {
            weekSummaryFirstDate.value = DateFormat.getDateInstance(DateFormat.MEDIUM).format(it)

            attendances.last()?.getDateAsObject()?.let {
                weekSummaryLastDate.value =  DateFormat.getDateInstance(DateFormat.MEDIUM).format(it)
            }
        }
    }

    private fun getExpectedLeaveTime(attendances: Attendance, workingHours: Double?) : String? {
        if (attendances.getDateAsObject()?.time?.let { DateUtils.isToday(it) } == false || workingHours == null || attendances.punches.isNullOrEmpty())
            return null

        val firstAttendanceDate = attendances.punches?.first()?.timestampObject.fromTaqniaFormatToDateObject()

        if (firstAttendanceDate == null)
            return null


            val leaveTimeCalender = Calendar.getInstance().apply {
                time = firstAttendanceDate
                add(Calendar.HOUR_OF_DAY,
                    workingHours.toString().substringBefore(".","0")?.trim()?.toInt() ?: 0)
                add(Calendar.MINUTE,
                    workingHours.toString().substringAfter(".","0")?.trim()?.toInt() ?: 0)
            }

        return if (leaveTimeCalender.timeInMillis >= Calendar.getInstance().timeInMillis)
            DateFormat.getTimeInstance().format(Date(leaveTimeCalender.timeInMillis))
    else
            "00.00"
    }

        private fun String?.getDisplayWorkingHours() : Double =
        try {
            LogsUtil.printErrorLog("all last week","${this?.substringBefore(":","0")}.${this?.substringAfter(":","0")?.substringBefore(":","0")}f".toFloat().toString())
            "${this?.substringBefore(":","0")?.trim()}.${this?.substringAfter(":","0")?.substringBefore(":","0")?.trim()}".toDouble()
        } catch (e : Exception) {
            Timber.e(e)
            08.00
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
        const val DEFAULT_WORKING_HOURS = 08.00
    }
}