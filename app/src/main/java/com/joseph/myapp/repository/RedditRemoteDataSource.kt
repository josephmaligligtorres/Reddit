package com.joseph.myapp.repository

import android.content.Context
import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.data.local.RedditDao
import com.joseph.myapp.data.remote.api.endpoint.DataApi
import com.joseph.myapp.helper.ResponseResult
import com.joseph.myapp.helper.decodeNetworkError
import com.joseph.myapp.helper.decodeUnknownError
import com.joseph.myapp.helper.getHttpStatus
import java.lang.ref.WeakReference
import kotlinx.coroutines.flow.Flow

class RedditRemoteDataSource(
    private val redditDao: RedditDao,
    private val context: WeakReference<Context>,
    private val api: DataApi
) : RedditDataSource {
    override suspend fun getReddits(): ResponseResult<Unit> {
        return when (val response = api.getSubreddits()) {
            is NetworkResponse.Success -> {
                val reddits = response.body.toReddits()
                for (item in reddits) {
                    redditDao.deleteReddit(item.uniqueId)
                    redditDao.insertReddit(item)
                }
                ResponseResult.Success(Unit)
            }
            is NetworkResponse.ServerError -> {
                ResponseResult.Error(getHttpStatus(response.code)?.reasonPhrase.toString())
            }
            is NetworkResponse.NetworkError -> {
                ResponseResult.Error(decodeNetworkError(response.error, context))
            }
            is NetworkResponse.UnknownError -> {
                ResponseResult.Error(decodeUnknownError(response.error, context))
            }
        }
    }

    override fun getLocalReddits(): Flow<List<Reddit>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertReddit(reddit: Reddit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReddit(reddit: Reddit) {
        TODO("Not yet implemented")
    }
}