package com.example.taqniaattendance.data.model.login

import android.os.Build
import android.os.Parcelable
import com.example.taqniaattendance.util.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginRequest(
    @SerializedName("user_id")
    @Expose
    val userID: String? = null,

    @SerializedName("otp")
    @Expose
    val password: String? = null,

    @SerializedName("mobile_type")
    @Expose
    val mobileType: String? = Constants.RemoteMetaData.ANDROID,

    @SerializedName("mobile_model")
    @Expose
    val mobileModel: String? = Build.MODEL,

    @SerializedName("mobile_notification_type")
    @Expose
    val notificationType: String? = null) : Parcelable