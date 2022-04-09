package com.joseph.myapp.navigation

import androidx.navigation.NamedNavArgument

data class NavData(
    val route: String,
    val safeArgs: String,
    val arguments: List<NamedNavArgument>
)

interface NavComponent {
    fun component(vararg safeArgs: Any?): NavData
}