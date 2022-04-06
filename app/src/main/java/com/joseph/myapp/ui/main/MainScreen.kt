package com.joseph.myapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joseph.myapp.helper.TriggeredEffect
import com.joseph.myapp.theme.MyTheme

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
            onLoadSubreddits = onLoadSubreddits
        )
    }
}

@Composable
fun MainContent(
    uiState: MainUiState,
    onLoadSubreddits: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.Red)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onLoadSubreddits) {
            Text(text = "Click")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        MainContent(
            uiState = MainUiState(),
            onLoadSubreddits = {}
        )
    }
}
