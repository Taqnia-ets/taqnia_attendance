package com.example.taqniaattendance.data.source.remote.history

import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.history.HistoryResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private var apiService: HistoryApiServices

    init {

        val logger = HttpLoggingInterceptor() //For API Logging
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://att.ncrst.edu.sa/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService =  retrofit.create(HistoryApiServices::class.java)
    }

    companion object {
        private val apiClient: ApiClient = ApiClient()
        @Synchronized
        fun getInstance(): ApiClient {
            return apiClient
        }
    }

    suspend fun getHistory(historyRequest: HistoryRequest): HistoryResponse? =
        apiService.getHistory(historyRequest)

}