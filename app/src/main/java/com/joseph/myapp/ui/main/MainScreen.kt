package com.joseph.myapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.joseph.myapp.R
import com.joseph.myapp.data.local.Reddit
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
            onSearchInputChanged = onSearchInputChanged
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
                searchInput = uiState.searchInput,
                onSearchInputChanged = onSearchInputChanged
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
        ) {
            val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.progress_bar))
            val lottieProgress by animateLottieCompositionAsState(
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever,
                isPlaying = uiState.isLoadingSubreddits,
                speed = 1f,
                restartOnPlay = false
            )

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = uiState.isLoadingSubreddits),
                onRefresh = { onLoadSubreddits() }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .weight(1f)
                ) {
                    if (uiState.reddits.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxHeight()
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LottieAnimation(
                                    modifier = Modifier
                                        .size(200.dp),
                                    composition = lottieComposition,
                                    progress = lottieProgress
                                )
                            }
                        }
                    } else {
                        val result = if (uiState.searchInput.isNotEmpty()) {
                            uiState.searchedReddits
                        } else {
                            uiState.reddits
                        }

                        items(result.size) { index ->
                            RedditItem(
                                reddit = result[index]
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RedditItem(
    reddit: Reddit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .clickable { }
    ) {
        Spacer(
            modifier = Modifier
                .height(15.dp)
        )

        Text(
            text = reddit.title,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
            color = Color(0xFF336699)
        )

        Spacer(
            modifier = Modifier
                .height(5.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFEFF7FF),
            elevation = 3.dp
        ) {
            Text(
                modifier = Modifier
                    .padding(all = 10.dp),
                text = reddit.description,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                color = Color(0xFF336699)
            )
        }

        Spacer(
            modifier = Modifier
                .height(5.dp)
        )

        Text(
            text = stringResource(
                id = R.string.subscribers,
                String.format("%,d", reddit.subscribers)
            ),
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = Color(0xFF000000)
        )

        Spacer(
            modifier = Modifier
                .height(15.dp)
        )
    }

    Spacer(
        modifier = Modifier
            .background(Color(0xFFC6C6C6))
            .fillMaxWidth()
            .height(1.dp)
    )
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
