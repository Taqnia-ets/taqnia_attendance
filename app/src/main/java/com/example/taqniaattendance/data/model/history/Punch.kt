package com.example.taqniaattendance.data.model.history

import android.os.Parcelable
import com.example.taqniaattendance.R
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Punch(
    @Expose
    @SerializedName("Punch")
    val punch: String = "Unknown",
    @Expose
    @SerializedName("Status")
    val status: String = "Unknown",
    @Expose
    @SerializedName("Timestamp")
    val timestamp: String?? = null,
    @Expose
    @SerializedName("TimestampObject")
    val timestampObject: String? = null
) : Parcelable {

    fun getTime(): String = timestamp?.substring(11) ?: "Unknown"

    fun getPunchTypeIcon(): Int {
        return when (status) {
            Attendance.CHECK_IN -> R.drawable.ic_down_arrow
            Attendance.CHECK_OUT -> R.drawable.ic_arrow_up
            else -> R.drawable.ic_unkown
        }
    }

}