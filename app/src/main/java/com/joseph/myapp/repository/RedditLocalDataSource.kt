package com.joseph.myapp.repository

import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.data.local.RedditDao
import com.joseph.myapp.helper.ResponseResult
import kotlinx.coroutines.flow.Flow

class RedditLocalDataSource(
    private val redditDao: RedditDao
) : RedditDataSource {
    override suspend fun getReddits(): ResponseResult<Unit> {
        TODO("Not yet implemented")
    }

    override fun getLocalReddits(): Flow<List<Reddit>> {
        return redditDao.getLocalReddits()
    }

    override suspend fun insertReddit(reddit: Reddit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReddit(reddit: Reddit) {
        TODO("Not yet implemented")
    }
}