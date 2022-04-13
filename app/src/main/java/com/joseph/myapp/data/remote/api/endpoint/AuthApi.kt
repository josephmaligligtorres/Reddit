package com.joseph.myapp.data.remote.api.endpoint

import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.data.remote.dto.RefreshTokenResponse
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {
    @FormUrlEncoded
    @POST("api/v1/access_token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): NetworkResponse<RefreshTokenResponse, ResponseBody>
}