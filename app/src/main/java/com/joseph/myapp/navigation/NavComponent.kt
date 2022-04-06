package com.joseph.myapp.navigation

import androidx.annotation.Keep
import androidx.navigation.NamedNavArgument

@Keep
data class NavData(
    val route: String,
    val safeArgs: String,
    val arguments: List<NamedNavArgument>
)

interface NavComponent {
    fun component(vararg safeArgs: Any?): NavData
}