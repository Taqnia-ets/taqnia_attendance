package com.example.taqniaattendance.data.model

import com.example.taqniaattendance.util.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseNetResponse(@SerializedName("status")
                           @Expose
                              var status: String? = null,
                           @SerializedName("message")
                           @Expose
                           val message: String? = null) {
    fun isSuccessful() : Boolean = !(status.equals(Constants.ErrorConstants.ERROR, true))

}