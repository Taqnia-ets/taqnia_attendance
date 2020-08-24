package com.example.taqniaattendance.data.model.user

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DepartmentLocation (
    @Expose
    @SerializedName("geometry")
    val geometry: Geometry?,
    @Expose
    @SerializedName("type")
    val type: String?
) : Parcelable