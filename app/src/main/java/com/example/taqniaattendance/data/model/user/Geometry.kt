package com.example.taqniaattendance.data.model.user

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Parcelize
data class Geometry(
    @Expose
    @SerializedName("coordinates")
    val coordinates: List<List<List<Double?>?>?>?
) : Parcelable {

    fun calculatePolygonsAsLatLng(): List<LatLng?>? {
        var ringsAsLatLng: List<LatLng?>? = null
        coordinates?.forEachIndexed { ringsIndex, rings ->
            ringsAsLatLng = rings?.map {geometry -> LatLng(geometry?.get(1)!!, geometry[0]!!) }
        }
        return ringsAsLatLng
    }

    fun calculateCenterPoint(): LatLng {
        val latLngBounds = LatLngBounds.builder()
        calculatePolygonsAsLatLng()?.forEach {
            it?.run { latLngBounds.include(it) }
        }
        return latLngBounds.build().center
    }



}