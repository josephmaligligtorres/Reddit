package com.joseph.myapp.ui.reddit

import android.webkit.SslErrorHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joseph.myapp.navigation.NavDestination
import com.joseph.myapp.theme.MyTheme
import com.joseph.myapp.ui.common.SslErrorDialog
import com.joseph.myapp.ui.common.WebView

@Composable
fun RedditScreen(
    navDestination: NavDestination,
    viewModel: RedditViewModel
) {
    with(viewModel) {
        val uiState by uiState.collectAsState()

        RedditContent(
            uiState = uiState,
            onStoreHandler = onStoreHandler,
            onBack = navDestination.onBack
        )
    }
}

@Composable
fun RedditContent(
    uiState: RedditUiState,
    onStoreHandler: (SslErrorHandler?, String) -> Unit,
    onBack: () -> Unit
) {
    WebView(
        modifier = Modifier
            .fillMaxSize(),
        url = "https://www.reddit.com/r/${uiState.reddit.displayName}/",
        onStoreHandler = onStoreHandler
    )

    SslErrorDialog(
        handler = uiState.handler,
        errorTitle = uiState.errorTitle,
        onCloseDialog = {
            onStoreHandler(null, "")
        },
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        RedditContent(
            uiState = RedditUiState(),
            onStoreHandler = { _, _ -> },
            onBack = {}
        )
    }
}