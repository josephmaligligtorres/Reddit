package com.joseph.myapp.ui.reddit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.ui.common.WebView

@Composable
fun RedditScreen(
    reddit: Reddit
) {
    WebView(
        modifier = Modifier
            .fillMaxSize(),
        url = "https://www.reddit.com/r/${reddit.displayName}/"
    )
}