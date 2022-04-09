package com.joseph.myapp.api.request

import com.joseph.myapp.BuildConfig

data class RefreshTokenRequest(
    val grantType: String = "refresh_token",
    val refreshToken: String = BuildConfig.REFRESH_TOKEN
)