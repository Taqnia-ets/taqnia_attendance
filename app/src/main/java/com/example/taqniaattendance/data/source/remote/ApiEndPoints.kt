package com.example.taqniaattendance.data.source.remote

import com.example.taqniaattendance.data.model.BaseNetResponse
import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.history.HistoryResponse
import com.example.taqniaattendance.data.model.login.LoginRequest
import com.example.taqniaattendance.data.model.login.LoginResponse
import com.example.taqniaattendance.data.model.punch.NewPunch
import com.example.taqniaattendance.data.model.user.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiEndPoints {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("history")
    fun getHistory(@Body historyRequest: HistoryRequest): Call<HistoryResponse?>

    @GET("check")
    fun getUser(): Call<User>

    @POST("punch")
    fun punch(@Body newPunch: NewPunch): Call<BaseNetResponse>
}
