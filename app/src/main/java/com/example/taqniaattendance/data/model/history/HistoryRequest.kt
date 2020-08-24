package com.example.taqniaattendance.data.model.history

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryRequest(
    @Expose
    @SerializedName("year")
    val year: String?,
    @Expose
    @SerializedName("month")
    val month: String?
) : Parcelable