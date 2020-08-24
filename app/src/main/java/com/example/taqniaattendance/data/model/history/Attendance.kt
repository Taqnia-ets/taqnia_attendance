package com.example.taqniaattendance.data.model.history

import android.os.Parcelable
import com.example.taqniaattendance.R
import com.example.taqniaattendance.util.fromMMMDDYYYYToDate
import com.example.taqniaattendance.util.toMMMDDYYYYFormat
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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

    companion object {
        const val CHECK_IN = "Check-In"
        const val CHECK_OUT = "Check-Out"
    }
}