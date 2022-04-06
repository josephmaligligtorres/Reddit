package com.joseph.myapp.api

import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.data.SubredditsResponse
import okhttp3.ResponseBody
import retrofit2.http.GET

interface DataApi {
    @GET("subreddits")
    suspend fun getSubreddits(): NetworkResponse<SubredditsResponse, ResponseBody>
}