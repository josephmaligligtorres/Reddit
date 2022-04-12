package com.joseph.myapp.ui.reddit

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.joseph.myapp.domain.RedditUseCase
import com.joseph.myapp.helper.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class RedditUiState(
    val error: String = "",
    val errorTrigger: Boolean = false
)

@HiltViewModel
class RedditViewModel @Inject constructor(
    private val redditUseCase: RedditUseCase
) : BaseViewModel() {
    private val viewModelState = MutableStateFlow(RedditUiState())

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
}