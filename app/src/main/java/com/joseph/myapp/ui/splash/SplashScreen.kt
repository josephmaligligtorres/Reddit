package com.joseph.myapp.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joseph.myapp.helper.SPLASH_SCREEN_DELAY
import com.joseph.myapp.navigation.NavDirection
import com.joseph.myapp.theme.MyTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navDirection: NavDirection
) {
    LaunchedEffect(Unit) {
        delay(SPLASH_SCREEN_DELAY)
        navDirection.closeSplashToMain()
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    Column(
        modifier = Modifier
            .background(Color.Green)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        SplashContent()
    }
}
