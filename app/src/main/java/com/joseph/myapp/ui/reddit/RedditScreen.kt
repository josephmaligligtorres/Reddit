package com.joseph.myapp.ui.reddit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joseph.myapp.navigation.NavDestination
import com.joseph.myapp.theme.MyTheme
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
            onBack = navDestination.onBack
        )
    }
}

@Composable
fun RedditContent(
    uiState: RedditUiState,
    onBack: () -> Unit
) {
    WebView(
        modifier = Modifier
            .fillMaxSize(),
        url = "https://www.reddit.com/r/${uiState.reddit.displayName}/",
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        RedditContent(
            uiState = RedditUiState(),
            onBack = {}
        )
    }
}