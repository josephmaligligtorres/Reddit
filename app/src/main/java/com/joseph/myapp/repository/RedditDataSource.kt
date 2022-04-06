package com.joseph.myapp.repository

import com.joseph.myapp.data.RefreshTokenResponse
import com.joseph.myapp.data.SubredditsResponse
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.ResponseResult
import kotlinx.coroutines.flow.Flow

interface RedditDataSource {
    suspend fun getSubreddits(): ResponseResult<SubredditsResponse>

    suspend fun refreshToken(): ResponseResult<RefreshTokenResponse>

    fun getAllReddits(): Flow<List<Reddit>>

    suspend fun insertReddit(reddit: Reddit)

    suspend fun deleteReddit(reddit: Reddit)
}