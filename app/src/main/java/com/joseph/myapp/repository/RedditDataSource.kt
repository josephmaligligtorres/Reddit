package com.joseph.myapp.repository

import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.ResponseResult
import kotlinx.coroutines.flow.Flow

interface RedditDataSource {
    suspend fun getReddits(): ResponseResult<Unit>

    fun getLocalReddits(): Flow<List<Reddit>>

    suspend fun insertReddit(reddit: Reddit)

    suspend fun deleteReddit(reddit: Reddit)
}