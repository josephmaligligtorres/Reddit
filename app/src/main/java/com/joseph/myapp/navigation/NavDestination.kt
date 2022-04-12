package com.joseph.myapp.navigation

import androidx.navigation.NavHostController
import com.joseph.myapp.data.local.Reddit

class NavDestination(private val navController: NavHostController) {
    val onBack: () -> Unit = {
        navController.popBackStack()
    }

    val onCloseSplashToMain: () -> Unit = {
        navController.navigate(
            route = Screen.MainScreen.component().safeArgs
        ) {
            popUpTo(Screen.SplashScreen.component().route) {
                inclusive = true
            }
        }
    }

    val onMainToReddit: (Reddit) -> Unit = { reddit ->
        navController.navigate(
            route = Screen.RedditScreen.component(reddit).safeArgs
        )
    }
}