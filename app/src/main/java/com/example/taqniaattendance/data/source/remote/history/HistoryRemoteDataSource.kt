package com.example.taqniaattendance.data.source.remote.history

import androidx.paging.PagingSource
import com.example.taqniaattendance.BuildConfig
import com.example.taqniaattendance.data.model.history.Attendance
import com.example.taqniaattendance.data.model.history.HistoryRequest
import com.example.taqniaattendance.data.model.history.HistoryResponse
import com.example.taqniaattendance.data.source.remote.ApiEndPoints
import com.example.taqniaattendance.util.PrefsHelper
import com.example.taqniaattendance.util.isNullOrZero
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object HistoryRemoteDataSource : PagingSource<HistoryRequest, Attendance>()  {

    private var mEndpoints = ApiClient.getInstance()

    private val initialHistoryRequest: HistoryRequest by lazy {
        getInitHistoryRequest()
    }

    override suspend fun load(params: LoadParams<HistoryRequest>): LoadResult<HistoryRequest, Attendance> {
        try {
            val currentLoadingPageKey = params.key ?: initialHistoryRequest
            val response1 = mEndpoints.getHistory(currentLoadingPageKey)

//            val data = response1?.result?.values?.toList()?.reversed() ?: emptyList()
//            val responseData = data

            return LoadResult.Page(
                data = emptyList(),//todo change it
                prevKey = getPrevPaginationMonth(currentLoadingPageKey),
                nextKey = getNextPaginationMonth(currentLoadingPageKey)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


//    override suspend fun load(params: LoadParams<HistoryRequest>): LoadResult<HistoryRequest, List<Attendance>> {
//        try {
//            val currentLoadingPageKey = params.key ?: getPaginationMonth(null)
//            val response = mEndpoints.getHistory(currentLoadingPageKey)
//            val data = response.body()?.result?.values?.toList()?.reversed() ?: emptyList()
//
////            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
//
//            return LoadResult.Page(
//                data = response.body()?.result.values.toList(),
//                prevKey = null,
//                nextKey = null
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }

    private fun getNextPaginationMonth(historyRequest: HistoryRequest): HistoryRequest {
        val currentMonth = historyRequest.month?.toIntOrNull()
        val nextMonth = currentMonth?.minus(1)
        if (nextMonth.isNullOrZero()) {
            val prevYear = historyRequest.year?.toIntOrNull()
            val nextYear = prevYear?.minus(1)
            return HistoryRequest(nextYear.toString(), 12.toString())
        }
        return HistoryRequest(historyRequest.year, nextMonth.toString())
    }

    private fun getPrevPaginationMonth(historyRequest: HistoryRequest): HistoryRequest? {
        val currentMonth = historyRequest.month?.toIntOrNull()
        if (historyRequest.month.equals(initialHistoryRequest.month) &&
            historyRequest.year.equals(initialHistoryRequest.year)
            || currentMonth == null)
            return null

        if (currentMonth >= 12) {
            val currentYear = historyRequest.year?.toIntOrNull()
            val prevYear = currentYear?.plus(1)
            return HistoryRequest(prevYear.toString(), 1.toString())
        }

        val prevMonth = currentMonth?.plus(1)

        return HistoryRequest(historyRequest.year, prevMonth.toString())
    }

    private fun getInitHistoryRequest() : HistoryRequest {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1 //we add 1, according that Calendar.MONTH start from 0
        return HistoryRequest(calendar.get(Calendar.YEAR).toString(), month.toString())
    }

    private fun getRetrofitInstance(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getOkHttpClientInstance())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiEndPoints::class.java)

    private fun getOkHttpClientInstance(): OkHttpClient {
        OkHttpClient.Builder().apply {
            // Add interceptor only in Debug mode.
            if (BuildConfig.DEBUG) addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY))
            return build()
        }
    }
}