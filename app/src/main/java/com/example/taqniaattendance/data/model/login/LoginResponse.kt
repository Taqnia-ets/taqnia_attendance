package com.example.taqniaattendance.data.model.login

import android.os.Parcelable
import com.example.taqniaattendance.data.model.BaseNetResponse
import com.example.taqniaattendance.util.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("token")
    @Expose
    val token: String,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("response_type")
    @Expose
    val response_type: String?) : Parcelable {
    fun isSuccessful() : Boolean = !(status.equals(Constants.ErrorConstants.ERROR, true))
}