package com.joseph.myapp.helper

import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.BuildConfig
import com.joseph.myapp.api.AuthApi
import com.joseph.myapp.data.remote.RefreshTokenResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val authApi: AuthApi
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.code != 401) {
            return null
        }

        return runBlocking {
            when (val result = refreshToken()) {
                is ResponseResult.Success -> {
                    setBearerToken(result.data.accessToken)
                    response.request.newBuilder()
                        .header("Authorization", "bearer ${getBearerToken()}")
                        .build()
                }
                else -> null
            }
        }
    }

    private suspend fun refreshToken(): ResponseResult<RefreshTokenResponse> {
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
                ResponseResult.Error(response.error.toString())
            }
            is NetworkResponse.UnknownError -> {
                ResponseResult.Error(response.error.toString())
            }
        }
    }
}