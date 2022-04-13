package com.joseph.myapp.data.remote.api.endpoint

import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.data.remote.dto.SubredditsResponse
import okhttp3.ResponseBody
import retrofit2.http.GET

interface DataApi {
    @GET("subreddits")
    suspend fun getSubreddits(): NetworkResponse<SubredditsResponse, ResponseBody>
}