package com.joseph.myapp.repository

import com.joseph.myapp.data.RefreshTokenResponse
import com.joseph.myapp.data.SubredditsResponse
import com.joseph.myapp.helper.ResponseResult

interface RedditRepository {
    suspend fun getSubreddits(): ResponseResult<SubredditsResponse>

    suspend fun refreshToken(): ResponseResult<RefreshTokenResponse>
}