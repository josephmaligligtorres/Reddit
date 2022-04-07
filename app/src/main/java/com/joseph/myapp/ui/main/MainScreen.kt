package com.joseph.myapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.joseph.myapp.R
import com.joseph.myapp.helper.TriggeredEffect
import com.joseph.myapp.theme.MyTheme
import com.joseph.myapp.ui.common.TopBar

@Composable
fun MainScreen(
    scaffoldState: ScaffoldState,
    viewModel: MainViewModel
) {
    with(viewModel) {
        val uiState by uiState.collectAsState()

        TriggeredEffect(
            uiState = uiState,
            trigger = uiState.errorTrigger
        ) {
            scaffoldState.snackbarHostState.showSnackbar(uiState.errorMessage)
        }

        MainContent(
            uiState = uiState,
            onLoadSubreddits = onLoadSubreddits,
            onSearchInputChanged = {}
        )
    }
}

@Composable
fun MainContent(
    uiState: MainUiState,
    onLoadSubreddits: () -> Unit,
    onSearchInputChanged: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                onSearchInputChanged = onSearchInputChanged
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEFF7FF)),
            verticalArrangement = Arrangement.Center
        ) {
            val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.progress_bar))
            val lottieProgress by animateLottieCompositionAsState(
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever,
                isPlaying = uiState.isLoadingSubreddits,
                speed = 1f,
                restartOnPlay = false
            )

            if (uiState.reddits.isEmpty()) {
                LottieAnimation(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(200.dp),
                    composition = lottieComposition,
                    progress = lottieProgress
                )
            } else {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = uiState.isLoadingSubreddits),
                    onRefresh = { onLoadSubreddits() }
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(uiState.reddits.size) { index ->
                            Text(text = uiState.reddits[index].title, color = Color.Red)
                            Text(text = uiState.reddits[index].description, color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        MainContent(
            uiState = MainUiState(),
            onLoadSubreddits = {},
            onSearchInputChanged = {}
        )
    }
}
