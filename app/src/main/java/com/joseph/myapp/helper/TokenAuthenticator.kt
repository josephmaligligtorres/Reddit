package com.joseph.myapp.helper

import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.BuildConfig
import com.joseph.myapp.data.remote.api.endpoint.AuthApi
import com.joseph.myapp.data.remote.api.request.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class TokenAuthenticator : Authenticator {
    private var retryCounter = 0

    override fun authenticate(route: Route?, response: Response): Request? {
        if (retryCounter == TOKEN_AUTHENTICATOR_LIMIT) {
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
                api().refreshToken(
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

    private fun api(): AuthApi {
        return createApi(
            okHttpClient = createHttpClient(
                token = { Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET) },
                builder = createAuthApiHttpBuilder()
            ),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = BuildConfig.BASE_AUTH_API_URL
        )
    }
}