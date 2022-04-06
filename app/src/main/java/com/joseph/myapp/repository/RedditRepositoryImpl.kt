package com.joseph.myapp.repository

import com.joseph.myapp.data.RefreshTokenResponse
import com.joseph.myapp.data.SubredditsResponse
import com.joseph.myapp.helper.ResponseResult

class RedditRepositoryImpl(
    private val remoteDataSource: RedditRemoteDataSource
) : RedditRepository {
    override suspend fun getSubreddits(): ResponseResult<SubredditsResponse> {
        return remoteDataSource.getSubreddits()
    }

    override suspend fun refreshToken(): ResponseResult<RefreshTokenResponse> {
        return remoteDataSource.refreshToken()
    }
}