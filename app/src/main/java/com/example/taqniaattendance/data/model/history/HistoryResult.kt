package com.example.taqniaattendance.data.model.history

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryResult(
    @Expose
    @SerializedName("absent_days")
    val absentDays: String?,
    @Expose
    @SerializedName("attend_late_counts")
    val attendLateCounts: String?,
    @Expose
    @SerializedName("attendances")
    var attendances: Map<String, Attendance>? = null,
    @Expose
    @SerializedName("department")
    val department: String?,
    @Expose
    @SerializedName("email")
    val email: String?,
    @Expose
    @SerializedName("end_date")
    val endDate: String?,
    @Expose
    @SerializedName("leave_early_counts")
    val leaveEarlyCounts: String?,
    @Expose
    @SerializedName("name")
    val name: String?,
    @Expose
    @SerializedName("over_time_counts")
    val overTimeCounts: String?,
    @Expose
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @Expose
    @SerializedName("start_date")
    val startDate: String?,
    @Expose
    @SerializedName("sub_department")
    val subDepartment: String?,
    @Expose
    @SerializedName("total_attend_early_time")
    val totalAttendEarlyTime: String?,
    @Expose
    @SerializedName("total_attend_early_time_str")
    val totalAttendEarlyTimeStr: String?,
    @Expose
    @SerializedName("total_attend_late_time")
    val totalAttendLateTime: String?,
    @Expose
    @SerializedName("total_attend_late_time_str")
    val totalAttendLateTimeStr: String?,
    @Expose
    @SerializedName("total_department_working_hours")
    val totalDepartmentWorkingHours: String?,
    @Expose
    @SerializedName("total_employee_working_hours")
    val totalEmployeeWorkingHours: String?,
    @Expose
    @SerializedName("total_employee_working_time")
    val totalEmployeeWorkingTime: String?,
    @Expose
    @SerializedName("total_employee_working_time_str")
    val totalEmployeeWorkingTimeStr: String?,
    @Expose
    @SerializedName("total_leave_early_time")
    val totalLeaveEarlyTime: String?,
    @Expose
    @SerializedName("total_leave_early_time_str")
    val totalLeaveEarlyTimeStr: String?,
    @Expose
    @SerializedName("total_leave_late_time")
    val totalLeaveLateTime: String?,
    @Expose
    @SerializedName("total_leave_late_time_str")
    val totalLeaveLateTimeStr: String?,
    @Expose
    @SerializedName("total_over_time")
    val totalOverTime: String?,
    @Expose
    @SerializedName("total_over_time_str")
    val totalOverTimeStr: String?,
    @Expose
    @SerializedName("user_id")
    val userId: String?,
    @Expose
    @SerializedName("vacation_days")
    val vacationDays: String?
) : Parcelable