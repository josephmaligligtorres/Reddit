package com.joseph.myapp.navigation

import androidx.navigation.NavHostController

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
}