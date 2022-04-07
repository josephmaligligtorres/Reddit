package com.joseph.myapp.domain

import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.repository.RedditRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllRedditsUseCase @Inject constructor(
    private val repository: RedditRepository
) {
    operator fun invoke(): Flow<List<Reddit>> {
        return repository.getAllReddits()
    }
}