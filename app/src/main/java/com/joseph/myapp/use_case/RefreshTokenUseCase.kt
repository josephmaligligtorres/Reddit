package com.joseph.myapp.use_case

import com.joseph.myapp.data.remote.RefreshTokenResponse
import com.joseph.myapp.helper.ResponseResult
import com.joseph.myapp.repository.RedditRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val repository: RedditRepository
) {
    suspend operator fun invoke(): ResponseResult<RefreshTokenResponse> {
        return repository.refreshToken()
    }
}