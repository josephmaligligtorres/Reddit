package com.joseph.myapp.navigation

import androidx.navigation.NavHostController
import com.joseph.myapp.data.local.Reddit

class NavDirection(private val navController: NavHostController) {
    val closeSplashToMain: () -> Unit = {
        navController.navigate(
            route = Screen.MainScreen.component().safeArgs
        ) {
            popUpTo(Screen.SplashScreen.component().route) {
                inclusive = true
            }
        }
    }

    val mainToReddit: (Reddit) -> Unit = { reddit ->
        navController.navigate(
            route = Screen.RedditScreen.component(reddit).safeArgs
        )
    }
}