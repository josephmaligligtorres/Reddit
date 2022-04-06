package com.joseph.myapp.repository

import com.joseph.myapp.data.remote.RefreshTokenResponse
import com.joseph.myapp.data.remote.SubredditsResponse
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.ResponseResult
import kotlinx.coroutines.flow.Flow

interface RedditRepository {
    suspend fun getSubreddits(): ResponseResult<SubredditsResponse>

    suspend fun refreshToken(): ResponseResult<RefreshTokenResponse>

    fun getAllReddits(): Flow<List<Reddit>>
}