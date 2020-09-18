package com.example.taqniaattendance.data.model.history

import android.os.Parcelable
import com.example.taqniaattendance.R
import com.example.taqniaattendance.util.LogsUtil
import com.example.taqniaattendance.util.fromMMMDDYYYYToDate
import com.example.taqniaattendance.util.toIntOrZero
import com.example.taqniaattendance.util.toMMMDDYYYYFormat
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import java.lang.Exception
import java.time.Year
import java.util.*
import kotlin.math.roundToInt

@Parcelize
data class Attendance(
    @Expose
    @SerializedName("attend_early_time")
    val attendEarlyTime: String?,
    @Expose
    @SerializedName("attend_early_time_str")
    val attendEarlyTimeStr: String?,
    @Expose
    @SerializedName("attend_late_time")
    val attendLateTime: String?,
    @Expose
    @SerializedName("attend_late_time_str")
    val attendLateTimeStr: String?,
    @Expose
    @SerializedName("attendance_status")
    val attendanceStatus: String?,
    @Expose
    @SerializedName("attendances")
    var punches: List<Punch?>?,
    @Expose
    @SerializedName("date")
    val date: String?,
    @Expose
    @SerializedName("first_punch")
    val firstPunch: String?,
    @Expose
    @SerializedName("last_punch")
    val lastPunch: String?,
    @Expose
    @SerializedName("leave_early_time")
    val leaveEarlyTime: String?,
    @Expose
    @SerializedName("leave_early_time_str")
    val leaveEarlyTimeStr: String?,
    @Expose
    @SerializedName("leave_late_time")
    val leaveLateTime: String?,
    @Expose
    @SerializedName("leave_late_time_str")
    val leaveLateTimeStr: String?,
    @Expose
    @SerializedName("over_time")
    val overTime: String?,
    @Expose
    @SerializedName("over_time_str")
    val overTimeStr: String?,
    @Expose
    @SerializedName("working_hours")
    val workingHours: String?
) : Parcelable {

    fun getFormattedDate() = date.toMMMDDYYYYFormat()

    fun getDateAsObject() = date.fromMMMDDYYYYToDate()

    fun getDisplayWorkingHours() : String {
        try {
            var displayTime : String= "${workingHours?.substringBefore(":", "0")
                ?.trim()}.${workingHours?.substringAfter(":", "0")?.substringBefore(":", "0")
                ?.trim()}h"

            if (displayTime.first().equals('0', true))
                displayTime = displayTime.substring(1)

            return displayTime

        } catch (e: Exception) {
            Timber.e(e)
            return "0.0h"
        }
    }

    fun getEmployeeWorkingHoursAsDouble(): Double =         try {
        LogsUtil.printErrorLog("all last week","${workingHours?.substringBefore(":","0")}.${workingHours?.substringAfter(":","0")?.substringBefore(":","0")} h")
        "${workingHours?.substringBefore(":","0")?.trim()}.${workingHours?.substringAfter(":","0")?.substringBefore(":","0")?.trim()}".toDoubleOrNull() ?: 0.0
    } catch (e : Exception) {
        Timber.e(e)
        0.0
    }

    fun getEmployeeWorkingHoursPercentage(departmentWorkingHours: Double) : Int {
        try {
            val workingHoursPercentage =  (280 / departmentWorkingHours) * getEmployeeWorkingHoursAsDouble()
            return workingHoursPercentage.roundToInt()
        } catch (e : IllegalArgumentException) {
            Timber.e(e)
            return 0
        }
    }


    fun getDayName() : String {
        val date = getDateAsObject()
        val calendar = Calendar.getInstance().apply { this.time= date}
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)?.substring(0, 1) ?: ""
    }
    companion object {
        const val CHECK_IN = "Check-In"
        const val CHECK_OUT = "Check-Out"
    }
}