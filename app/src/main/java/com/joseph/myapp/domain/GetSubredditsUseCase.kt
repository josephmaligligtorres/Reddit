package com.joseph.myapp.domain

import com.joseph.myapp.data.remote.SubredditsResponse
import com.joseph.myapp.helper.ResponseResult
import com.joseph.myapp.repository.RedditRepository
import javax.inject.Inject

class GetSubredditsUseCase @Inject constructor(
    private val repository: RedditRepository
) {
    suspend operator fun invoke(): ResponseResult<SubredditsResponse> {
        return repository.getSubreddits()
    }
}