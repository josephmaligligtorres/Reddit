package com.joseph.myapp.ui.reddit

import android.webkit.SslErrorHandler
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
    val handler: SslErrorHandler? = null,
    val errorTitle: String = ""
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

    val onStoreHandler: (SslErrorHandler?, String) -> Unit = { handler, errorTitle ->
        viewModelState.update {
            it.copy(
                handler = handler,
                errorTitle = errorTitle
            )
        }
    }
}