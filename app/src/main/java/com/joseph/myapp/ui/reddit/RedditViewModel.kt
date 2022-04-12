package com.joseph.myapp.ui.reddit

import android.webkit.SslErrorHandler
import androidx.lifecycle.viewModelScope
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class RedditUiState(
    val reddit: Reddit = Reddit(),
    val handler: SslErrorHandler? = null,
    val errorTitle: String = ""
)

class RedditViewModel : BaseViewModel() {
    private val viewModelState = MutableStateFlow(RedditUiState())

    val uiState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewModelState.value
    )

    val onStoreReddit: (Reddit) -> Unit = { reddit ->
        viewModelState.update {
            it.copy(
                reddit = reddit
            )
        }
    }

    val onStoreHandler: (SslErrorHandler?, String) -> Unit = { handler, errorTitle ->
        viewModelState.update {
            it.copy(
                handler = handler,
                errorTitle = errorTitle
            )
        }
    }
}