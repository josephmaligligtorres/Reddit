package com.joseph.myapp.helper

import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.BuildConfig
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) {
            return null
        }

        return runBlocking {
            when (val token = refreshToken()) {
                is ResponseResult.Success -> {
                    setBearerToken(token.data)
                    response.request.newBuilder()
                        .header("Authorization", getBearerToken())
                        .build()
                }
                else -> {
                    null
                }
            }
        }
    }

    private suspend fun refreshToken(): ResponseResult<String> {
        return when (
            val response = createAuthApi().refreshToken(
                grantType = "refresh_token",
                refreshToken = BuildConfig.REFRESH_TOKEN
            )
        ) {
            is NetworkResponse.Success -> {
                ResponseResult.Success(response.body.accessToken)
            }
            else -> {
                ResponseResult.Error("")
            }
        }
    }
}