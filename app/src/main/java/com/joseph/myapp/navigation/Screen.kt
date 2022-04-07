package com.joseph.myapp.navigation

import com.joseph.myapp.data.local.Reddit
import com.joseph.myapp.helper.NavKey
import com.joseph.myapp.helper.generateNavArgument
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

    object RedditScreen : Screen("Screen.RedditScreen") {
        override fun component(vararg safeArgs: Any?): NavData {
            return generateNavData(
                route = route,
                safeArgs = safeArgs,
                arguments = listOf(
                    generateNavArgument(
                        key = NavKey.PARCELIZE_REDDIT.value,
                        dataType = CustomNavType.DataClassType,
                        isNullable = false,
                        initialValue = Reddit(0, "", "", "", 0L)
                    )
                )
            )
        }
    }
}
