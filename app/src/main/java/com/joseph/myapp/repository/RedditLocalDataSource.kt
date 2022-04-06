package com.joseph.myapp.repository

import com.joseph.myapp.data.RefreshTokenResponse
import com.joseph.myapp.data.SubredditsResponse
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.data.local.RedditDao
import com.joseph.myapp.helper.ResponseResult
import kotlinx.coroutines.flow.Flow

class RedditLocalDataSource(
    private val redditDao: RedditDao
) : RedditDataSource {
    override suspend fun getSubreddits(): ResponseResult<SubredditsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(): ResponseResult<RefreshTokenResponse> {
        TODO("Not yet implemented")
    }

    override fun getAllReddits(): Flow<List<Reddit>> {
        return redditDao.getAllReddits()
    }

    override suspend fun insertReddit(reddit: Reddit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReddit(reddit: Reddit) {
        TODO("Not yet implemented")
    }
}