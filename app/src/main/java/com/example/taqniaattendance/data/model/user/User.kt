package com.example.taqniaattendance.data.model.user

import android.os.Parcelable
import com.example.taqniaattendance.data.model.BaseNetResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("user_id")
    var id: String? = null,
    @SerializedName("password")
    @Expose
    var password: String? = null,
    @SerializedName("department_working_hours")
    @Expose
    val workingHours: String? = null,
    @Expose
    @SerializedName("check_in")
    val checkIn: Boolean? = null,
    @Expose
    @SerializedName("check_out")
    val checkOut: Boolean? = null,
    @Expose
    @SerializedName("department")
    val department: String? = null,
    @Expose
    @SerializedName("email")
    val email: String? = null,
    @Expose
    @SerializedName("name")
    val name: String? = null,
    @Expose
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @Expose
    @SerializedName("strict_punch")
    val strictPunch: Boolean? = null,
    @Expose
    @SerializedName("sub_department")
    val subDepartment: String? = null,
    @Expose
    @SerializedName("department_location")
    val departmentLocation: DepartmentLocation? = null): BaseNetResponse(), Parcelable
