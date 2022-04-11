package com.joseph.myapp.repository

import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.ResponseResult
import kotlinx.coroutines.flow.Flow

class RedditRepositoryImpl(
    private val remoteDataSource: RedditRemoteDataSource,
    private val localDataSource: RedditLocalDataSource,
) : RedditRepository {
    override suspend fun getReddits(): ResponseResult<Unit> {
        return remoteDataSource.getReddits()
    }

    override fun getLocalReddits(): Flow<List<Reddit>> {
        return localDataSource.getLocalReddits()
    }
}