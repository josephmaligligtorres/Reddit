package com.joseph.myapp.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.joseph.myapp.helper.BaseComponentActivity
import com.joseph.myapp.navigation.NavDirection
import com.joseph.myapp.navigation.SetupNavGraph
import com.joseph.myapp.theme.MyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            MyTheme {
                val navController = rememberNavController()
                val navDirection = NavDirection(navController)
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    scaffoldState = scaffoldState
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        SetupNavGraph(
                            navController = navController,
                            navDirection = navDirection,
                            scaffoldState = scaffoldState
                        )
                    }
                }
            }
        }
    }
}
