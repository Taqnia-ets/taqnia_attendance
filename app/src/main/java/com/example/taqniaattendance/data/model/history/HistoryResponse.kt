package com.example.taqniaattendance.data.model.history

import android.os.Parcelable
import com.example.taqniaattendance.util.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryResponse(@SerializedName("status")
                              var status: String? = null,
                              @SerializedName("result")
                              var result: HistoryResult? = null,
                              @SerializedName("message")
                               var message: String? = null) : Parcelable {
    fun isSuccessful() : Boolean = !(status.equals(Constants.ErrorConstants.ERROR, true))

}