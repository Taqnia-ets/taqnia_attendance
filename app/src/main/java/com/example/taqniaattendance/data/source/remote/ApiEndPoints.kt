package com.example.taqniaattendance.data.source.remote

import com.example.taqniaattendance.data.model.BaseNetResponse
import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.history.HistoryResponse
import com.example.taqniaattendance.data.model.login.LoginRequest
import com.example.taqniaattendance.data.model.login.LoginResponse
import com.example.taqniaattendance.data.model.punch.NewPunch
import com.example.taqniaattendance.data.model.user.User
import retrofit2.Call
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
//
//    @GET("farms/GetByFarExtent")
//    fun getFarFarms(@Query("maxX") maxX: Double,
//                    @Query("maxY") maxY: Double,
//                    @Query("minX") minX: Double,
//                    @Query("minY") minY: Double): Call<BaseNetResponse<List<Activity<FarmAttributes?>?>?>?>
//
//    @GET("farms/GetByCloseExtent")
//    fun getByCloseExtent(@Query("maxX") maxX: Double,
//                         @Query("maxY") maxY: Double,
//                         @Query("minX") minX: Double,
//                         @Query("minY") minY: Double): Call<BaseNetResponse<CloseExtentResponse?>?>
//
//    @GET("farms/GetDataByExtent")
//    fun getFullDateByExtent(@Query("maxX") maxX: Double,
//                            @Query("maxY") maxY: Double,
//                            @Query("minX") minX: Double,
//                            @Query("minY") minY: Double): Call<BaseNetResponse<List<FullDataResponse?>?>?>
//
//    @GET("lookups/get")
//    fun getLookup(): Call<Lookup?>
//
//    @POST("Fishes/Add")
//    fun addFishes(@Body fishes : List<Fishes>) : Call<BaseNetResponse<List<BaseResult?>?>?>
//
//    @POST("Fishes/Update")
//    fun updateFishes(@Body fishes : List<Fishes>) : Call<BaseNetResponse<List<BaseResult?>?>?>
//
//    @POST("LocationInfo/Add")
//    fun addLocationInfo(@Body locationInfo : List<LocationInfo>) : Call<BaseNetResponse<List<BaseResult?>?>?>
//
//    @POST("LocationInfo/Update")
//    fun updateLocationInfo(@Body locationInfo : List<LocationInfo>) : Call<BaseNetResponse<List<BaseResult?>?>?>
//
//    @POST("OwnershipInfo/Add")
//    fun addOwnershipInfo(@Body ownershipInfo : List<OwnershipInfo>) : Call<BaseNetResponse<List<BaseResult?>?>?>
//
//    @POST("OwnershipInfo/Update")
//    fun updateOwnershipInfo(@Body ownershipInfo : List<OwnershipInfo>) : Call<BaseNetResponse<List<BaseResult?>?>?>
//
//    @POST("{route}/Delete")
//    fun deleteActivity(@Path("route") route: String, @Body id: HashMap<String, Int>) : Call<BaseNetResponse<List<BaseResult?>?>?>
}
