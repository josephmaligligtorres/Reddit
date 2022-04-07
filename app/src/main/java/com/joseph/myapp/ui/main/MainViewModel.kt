package com.joseph.myapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.ResponseResult
import com.joseph.myapp.navigation.NavDirection
import com.joseph.myapp.domain.GetAllRedditsUseCase
import com.joseph.myapp.domain.GetSubredditsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MainUiState(
    val reddits: List<Reddit> = listOf(),
    val searchedReddits: List<Reddit> = listOf(),
    val isLoadingSubreddits: Boolean = false,
    val searchInput: String = "",
    val errorMessage: String = "",
    val errorTrigger: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSubredditsUseCase: GetSubredditsUseCase,
    private val getAllRedditsUseCase: GetAllRedditsUseCase
) : ViewModel() {
    private lateinit var navDirection: NavDirection
    private val viewModelState = MutableStateFlow(MainUiState())
    val uiState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewModelState.value
    )

    val onSetup: (NavDirection) -> Unit = { navDirection ->
        this.navDirection = navDirection
    }

    val onLoadSubreddits: () -> Unit = {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    isLoadingSubreddits = true
                )
            }

            when (val result = withContext(Dispatchers.IO) { getSubredditsUseCase() }) {
                is ResponseResult.Error -> {
                    viewModelState.update {
                        it.copy(
                            errorMessage = result.message,
                            errorTrigger = !it.errorTrigger
                        )
                    }
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

    val onNavigateMainToReddit: (Reddit) -> Unit = { reddit ->
        navDirection.mainToReddit(reddit)
    }

    private fun getState(): MainUiState {
        return viewModelState.value
    }

    init {
        viewModelScope.launch {
            getAllRedditsUseCase().collect { reddits ->
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