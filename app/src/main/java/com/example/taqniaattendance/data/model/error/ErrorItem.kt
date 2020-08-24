package com.kacst.hsr.data.model.error

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorItem(
    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("field_name")
    @Expose
    val fieldName: String): Parcelable