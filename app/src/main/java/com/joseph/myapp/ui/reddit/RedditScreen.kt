package com.joseph.myapp.ui.reddit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.TriggeredEffect
import com.joseph.myapp.navigation.NavDestination
import com.joseph.myapp.ui.common.WebView
import com.joseph.myapp.ui.main.MainContent

@Composable
fun RedditScreen(
    navDestination: NavDestination,
    scaffoldState: ScaffoldState,
    viewModel: RedditViewModel,
    reddit: Reddit
) {
    with(viewModel) {
        val uiState by uiState.collectAsState()

        with(uiState) {
            TriggeredEffect(
                input = error,
                trigger = errorTrigger
            ) {
                scaffoldState.snackbarHostState.showSnackbar(error)
            }

            RedditContent(
                reddit = reddit,
                onTriggerError = onTriggerError
            )
        }
    }

}

@Composable
fun RedditContent(
    reddit: Reddit,
    onTriggerError: (String) -> Unit
) {
    val url = "https://octo.cimbbank.com.ph"
    WebView(
        modifier = Modifier
            .fillMaxSize(),
        url = url, //"https://www.reddit.com/r/${reddit.displayName}/",
        onTriggerError = onTriggerError
    )
}