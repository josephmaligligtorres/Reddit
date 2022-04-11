package com.joseph.myapp.repository

import android.content.Context
import com.haroldadmin.cnradapter.NetworkResponse
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.data.local.RedditDao
import com.joseph.myapp.helper.ResponseResult
import com.joseph.myapp.helper.createDataApi
import com.joseph.myapp.helper.decodeNetworkError
import com.joseph.myapp.helper.decodeUnknownError
import com.joseph.myapp.helper.getHttpStatus
import java.lang.ref.WeakReference
import kotlinx.coroutines.flow.Flow

class RedditRemoteDataSource(
    private val redditDao: RedditDao,
    private val context: WeakReference<Context>
) : RedditDataSource {
    override suspend fun getReddits(): ResponseResult<Unit> {
        return when (val response = createDataApi().getSubreddits()) {
            is NetworkResponse.Success -> {
                val reddits = response.body.toReddits()
                for (item in reddits) {
                    redditDao.deleteReddit(item.uniqueId)
                    redditDao.insertReddit(item)
                }
                ResponseResult.Success(Unit)
            }
            is NetworkResponse.ServerError -> {
                val message = getHttpStatus(response.code)?.reasonPhrase.toString()
                ResponseResult.Error(message)
            }
            is NetworkResponse.NetworkError -> {
                val message = decodeNetworkError(response.error, context)
                ResponseResult.Error(message)
            }
            is NetworkResponse.UnknownError -> {
                val message = decodeUnknownError(response.error, context)
                ResponseResult.Error(message)
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