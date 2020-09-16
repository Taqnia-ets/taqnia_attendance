package com.example.taqniaattendance.data.model.punch

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewPunch(
    @Expose
    @SerializedName("punch_type")
    val punchType: String?,
    @Expose
    @SerializedName("latitude")
    val latitude: String?,
    @Expose
    @SerializedName("longitude")
    val longitude: String?
) : Parcelable