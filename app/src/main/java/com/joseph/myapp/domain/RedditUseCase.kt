package com.joseph.myapp.domain

import com.joseph.myapp.repository.RedditRepository
import javax.inject.Inject

enum class RedditUseCaseType {
    GET_REMOTE_SUBREDDITS,
    GET_LOCAL_SUBREDDITS
}

class RedditUseCase @Inject constructor(
    private val repository: RedditRepository
) {
    suspend operator fun <T> invoke(type: RedditUseCaseType, vararg args: Any): T {
        return when (type) {
            RedditUseCaseType.GET_REMOTE_SUBREDDITS -> {
                repository.getReddits() as T
            }
            RedditUseCaseType.GET_LOCAL_SUBREDDITS -> {
                repository.getLocalReddits() as T
            }
        }
    }
}