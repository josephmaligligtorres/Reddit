package com.joseph.myapp.repository

import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.ResponseResult
import kotlinx.coroutines.flow.Flow

interface RedditRepository {
    suspend fun getReddits(): ResponseResult<Unit>

    fun getLocalReddits(): Flow<List<Reddit>>
}