package com.kacst.hsr.data.model.error

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppError(
    @SerializedName("status_code")
    @Expose
    var code: Int? = null,

    @SerializedName("error_message")
    @Expose
    var message: String? = null,

    @SerializedName("errors")
    @Expose
    var elements: List<ErrorItem>? = null) : Parcelable