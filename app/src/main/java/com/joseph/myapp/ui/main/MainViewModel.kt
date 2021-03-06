package com.joseph.myapp.ui.main

import androidx.lifecycle.viewModelScope
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.ResponseResult
import com.joseph.myapp.domain.RedditUseCase
import com.joseph.myapp.domain.RedditUseCaseType
import com.joseph.myapp.helper.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MainUiState(
    val reddits: List<Reddit> = listOf(),
    val searchedReddits: List<Reddit> = listOf(),
    val isLoadingSubreddits: Boolean = false,
    val searchInput: String = "",
    val error: String = "",
    val errorTrigger: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val redditUseCase: RedditUseCase
) : BaseViewModel() {
    private val viewModelState = MutableStateFlow(MainUiState())

    val uiState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewModelState.value
    )

    val onTriggerError: (String) -> Unit = { error ->
        viewModelState.update {
            it.copy(
                error = error,
                errorTrigger = !it.errorTrigger
            )
        }
    }

    val onLoadSubreddits: () -> Unit = {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    isLoadingSubreddits = true
                )
            }

            when (
                val result = withContext(Dispatchers.IO) {
                    redditUseCase<ResponseResult<Unit>>(
                        type = RedditUseCaseType.GET_REMOTE_SUBREDDITS
                    )
                }
            ) {
                is ResponseResult.Error -> {
                    onTriggerError(result.error)
                }
            }

            viewModelState.update {
                it.copy(
                    isLoadingSubreddits = false
                )
            }
        }
    }

    val onSearchInputChanged: (String) -> Unit = { searchInput ->
        if (searchInput.isNotEmpty() && getState().reddits.isNotEmpty()) {
            val result = getState().reddits.filter {
                it.title.contains(searchInput, ignoreCase = true)
            }

            viewModelState.update {
                it.copy(
                    searchedReddits = result
                )
            }
        }

        viewModelState.update {
            it.copy(
                searchInput = searchInput
            )
        }
    }

    private fun getState(): MainUiState {
        return viewModelState.value
    }

    init {
        viewModelScope.launch {
            redditUseCase<Flow<List<Reddit>>>(
                type = RedditUseCaseType.GET_LOCAL_SUBREDDITS
            ).collect { reddits ->
                viewModelState.update {
                    it.copy(
                        reddits = reddits
                    )
                }
            }
        }

        onLoadSubreddits()
    }
}