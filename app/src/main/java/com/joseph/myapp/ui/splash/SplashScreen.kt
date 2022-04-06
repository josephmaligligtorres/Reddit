package com.joseph.myapp.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseph.myapp.R
import com.joseph.myapp.helper.InitEffect
import com.joseph.myapp.helper.SPLASH_SCREEN_DELAY
import com.joseph.myapp.navigation.NavDirection
import com.joseph.myapp.theme.MyTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navDirection: NavDirection
) {
    InitEffect {
        delay(SPLASH_SCREEN_DELAY)
        navDirection.closeSplashToMain()
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .wrapContentSize(),
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        SplashContent()
    }
}
