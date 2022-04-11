package com.joseph.myapp.helper

import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.api.request.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {
    private var retryCounter = 0

    override fun authenticate(route: Route?, response: Response): Request? {
        if (getHttpStatus(response.code) != HttpStatus.UNAUTHORIZED || retryCounter == TOKEN_AUTHENTICATOR_LIMIT) {
            return null
        }

        retryCounter++

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
            val response = with(RefreshTokenRequest()) {
                createAuthApi().refreshToken(
                    grantType = grantType,
                    refreshToken = refreshToken
                )
            }
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