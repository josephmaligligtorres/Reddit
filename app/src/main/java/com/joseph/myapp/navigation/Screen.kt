package com.joseph.myapp.navigation

import com.joseph.myapp.helper.generateNavData

sealed class Screen(protected val route: String) : NavComponent {
    object SplashScreen : Screen("Screen.SplashScreen") {
        override fun component(vararg safeArgs: Any?): NavData {
            return generateNavData(
                route = route
            )
        }
    }

    object MainScreen : Screen("Screen.MainScreen") {
        override fun component(vararg safeArgs: Any?): NavData {
            return generateNavData(
                route = route
            )
        }
    }
}
