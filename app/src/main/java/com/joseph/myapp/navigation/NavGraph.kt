package com.joseph.myapp.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.NavKey
import com.joseph.myapp.helper.safeArgsDataClass
import com.joseph.myapp.ui.main.MainScreen
import com.joseph.myapp.ui.main.MainViewModel
import com.joseph.myapp.ui.reddit.RedditScreen
import com.joseph.myapp.ui.splash.SplashScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    navDirection: NavDirection,
    scaffoldState: ScaffoldState
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.component().route
    ) {
        composable(
            route = Screen.SplashScreen.component().route
        ) {
            SplashScreen(
                navDirection = navDirection
            )
        }

        composable(
            route = Screen.MainScreen.component().route
        ) {
            val viewModel: MainViewModel = hiltViewModel()
            viewModel.onSetup(navDirection)
            MainScreen(
                scaffoldState = scaffoldState,
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.RedditScreen.component().route,
            arguments = Screen.RedditScreen.component().arguments
        ) {
            val reddit = it.safeArgsDataClass<Reddit>(NavKey.PARCELIZE_REDDIT.value) ?: Reddit()
            RedditScreen(
                reddit = reddit
            )
        }
    }
}