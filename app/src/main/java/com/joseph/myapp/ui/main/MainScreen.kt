package com.joseph.myapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
        Box(
            modifier = Modifier
                .background(Color(0xFFEFF7FF))
                .fillMaxSize()
        )
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
