package com.joseph.myapp.repository

import android.content.Context
import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.BuildConfig
import com.joseph.myapp.api.AuthApi
import com.joseph.myapp.api.DataApi
import com.joseph.myapp.data.RefreshTokenResponse
import com.joseph.myapp.data.SubredditsResponse
import com.joseph.myapp.helper.ResponseResult
import com.joseph.myapp.helper.decodeNetworkError
import com.joseph.myapp.helper.decodeUnknownError
import com.joseph.myapp.helper.getHttpStatus
import java.lang.ref.WeakReference

class RedditRemoteDataSource(
    private val authApi: AuthApi,
    private val dataApi: DataApi,
    private val context: WeakReference<Context>
) : RedditDataSource {
    override suspend fun getSubreddits(): ResponseResult<SubredditsResponse> {
        return when (val response = dataApi.getSubreddits()) {
            is NetworkResponse.Success -> {
                ResponseResult.Success(response.body)
            }
            is NetworkResponse.ServerError -> {
                val message = getHttpStatus(response.code)?.reasonPhrase.toString()
                ResponseResult.Error(message)
            }
            is NetworkResponse.NetworkError -> {
                val message = decodeNetworkError(response.error, context)
                ResponseResult.Error(message)
            }
            is NetworkResponse.UnknownError -> {
                val message = decodeUnknownError(response.error, context)
                ResponseResult.Error(message)
            }
        }
    }

    override suspend fun refreshToken(): ResponseResult<RefreshTokenResponse> {
        return when (
            val response = authApi.refreshToken(
                grantType = "refresh_token",
                refreshToken = BuildConfig.REFRESH_TOKEN
            )
        ) {
            is NetworkResponse.Success -> {
                ResponseResult.Success(response.body)
            }
            is NetworkResponse.ServerError -> {
                val message = getHttpStatus(response.code)?.reasonPhrase.toString()
                ResponseResult.Error(message)
            }
            is NetworkResponse.NetworkError -> {
                val message = decodeNetworkError(response.error, context)
                ResponseResult.Error(message)
            }
            is NetworkResponse.UnknownError -> {
                val message = decodeUnknownError(response.error, context)
                ResponseResult.Error(message)
            }
        }
    }
}