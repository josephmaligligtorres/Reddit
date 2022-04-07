package com.joseph.myapp.repository

import com.joseph.myapp.data.remote.SubredditsResponse
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.ResponseResult
import kotlinx.coroutines.flow.Flow

class RedditRepositoryImpl(
    private val remoteDataSource: RedditRemoteDataSource,
    private val localDataSource: RedditLocalDataSource,
) : RedditRepository {
    override suspend fun getSubreddits(): ResponseResult<SubredditsResponse> {
        return remoteDataSource.getSubreddits()
    }

    override fun getAllReddits(): Flow<List<Reddit>> {
        return localDataSource.getAllReddits()
    }
}